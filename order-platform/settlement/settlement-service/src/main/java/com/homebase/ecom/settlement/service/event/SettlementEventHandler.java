package com.homebase.ecom.settlement.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.infrastructure.persistence.adapter.SettlementJpaRepository;
import com.homebase.ecom.settlement.infrastructure.persistence.mapper.SettlementMapper;
import com.homebase.ecom.shared.Money;
import com.homebase.ecom.shared.CurrencyResolver;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import com.homebase.ecom.core.workflow.HmStateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Chenile event handler for settlement cross-service events.
 * Registered via settlementEventService.json.
 *
 * CONSUMES:
 * - order.events (ORDER_COMPLETED -> create settlement)
 * - payment.events (PAYMENT_REFUNDED -> adjust settlement)
 *
 * PUBLISHES to settlement.events:
 * - SETTLEMENT_CALCULATED, SETTLEMENT_DISBURSED, SETTLEMENT_DISPUTED (via PostSaveHooks)
 */
public class SettlementEventHandler {

    private static final Logger log = LoggerFactory.getLogger(SettlementEventHandler.class);

    private final HmStateEntityServiceImpl<Settlement> settlementStateEntityService;
    private final SettlementJpaRepository settlementJpaRepository;
    private final SettlementMapper settlementMapper;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;
    private final CurrencyResolver currencyResolver;

    public SettlementEventHandler(
            @Qualifier("_settlementStateEntityService_") HmStateEntityServiceImpl<Settlement> settlementStateEntityService,
            SettlementJpaRepository settlementJpaRepository,
            SettlementMapper settlementMapper,
            ChenilePub chenilePub,
            ObjectMapper objectMapper,
            CurrencyResolver currencyResolver) {
        this.settlementStateEntityService = settlementStateEntityService;
        this.settlementJpaRepository = settlementJpaRepository;
        this.settlementMapper = settlementMapper;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
        this.currencyResolver = currencyResolver;
    }

    // ── order.events ──────────────────────────────────────────────────────

    @Transactional
    public void handleOrderEvent(OrderCompletedEvent event) {
        if (event == null || event.getOrderId() == null) return;

        log.info("Settlement: received OrderCompletedEvent for order: {}", event.getOrderId());

        // Check idempotency — settlement for this order already exists?
        if (settlementJpaRepository.findByOrderId(event.getOrderId()).isPresent()) {
            log.warn("Idempotency: settlement already exists for order {} (possible replay). Skipping.",
                    event.getOrderId());
            return;
        }

        try {
            // Group items by supplier and create a settlement per supplier
            Map<String, BigDecimal> supplierTotals = new java.util.HashMap<>();
            if (event.getItems() != null) {
                for (OrderCompletedEvent.CompletedItem item : event.getItems()) {
                    supplierTotals.merge(item.getSupplierId(), item.getAmount(), BigDecimal::add);
                }
            }

            String currency = currencyResolver.resolve().code();

            for (Map.Entry<String, BigDecimal> entry : supplierTotals.entrySet()) {
                String supplierId = entry.getKey();
                BigDecimal orderAmount = entry.getValue();

                Settlement settlement = new Settlement();
                settlement.setSupplierId(supplierId);
                settlement.setOrderId(event.getOrderId());
                // Convert BigDecimal amount (assumed paise) to Money
                settlement.setOrderAmount(Money.of(orderAmount.longValue(), currency));
                settlement.setCurrency(currency);
                settlement.setDescription("Settlement for order " + event.getOrderId());
                settlement.setSettlementPeriodStart(LocalDate.now());
                settlement.setSettlementPeriodEnd(LocalDate.now().plusDays(14));

                // Create through STM — sets initial state (PENDING) and persists
                settlementStateEntityService.process(settlement, null, null);
                log.info("Created settlement for supplier {} from order {}, amount {} paise",
                        supplierId, event.getOrderId(), orderAmount);
            }
        } catch (RuntimeException e) {
            log.warn("Idempotency: settlement creation for order {} already completed. Detail: {}",
                    event.getOrderId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to create settlement for order: {}", event.getOrderId(), e);
        }
    }

    // ── payment.events ────────────────────────────────────────────────────

    public void handlePaymentEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) return;

        if (PaymentRefundedEvent.EVENT_TYPE.equals(envelope.getEventType())) {
            try {
                PaymentRefundedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), PaymentRefundedEvent.class);
                log.info("Settlement: received PaymentRefundedEvent for order: {}", event.getOrderId());

                // Find settlement for this order and add adjustment
                settlementJpaRepository.findByOrderId(event.getOrderId()).ifPresent(entity -> {
                    Settlement settlement = settlementMapper.toModel(entity);

                    // Refund amount is a deduction (negate)
                    BigDecimal adjustmentAmount = event.getRefundedAmount().negate();
                    com.homebase.ecom.settlement.model.SettlementAdjustment adjustment =
                            new com.homebase.ecom.settlement.model.SettlementAdjustment(
                                    adjustmentAmount,
                                    "Refund: " + event.getRefundReason(),
                                    "SYSTEM");
                    settlement.getAdjustments().add(adjustment);

                    // Recalculate net if already calculated (amounts in paise)
                    if (settlement.getNetAmount() != null) {
                        long currentNetPaise = settlement.getNetAmount().getAmount();
                        long adjustmentPaise = adjustmentAmount.longValue();
                        settlement.setNetAmount(Money.of(currentNetPaise + adjustmentPaise, settlement.getCurrency()));
                    }

                    log.info("Added refund adjustment {} paise to settlement {} for order {}",
                            adjustmentAmount, settlement.getId(), event.getOrderId());
                });
            } catch (Exception e) {
                log.error("Error processing payment refund event: {}", envelope.getEventType(), e);
            }
        }
    }

    // ── Publishing helpers ────────────────────────────────────────────────

    private void publish(String topic, String key, Object payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            chenilePub.publish(topic, body, Map.of("key", key));
        } catch (JacksonException e) {
            log.error("Failed to serialize event for topic={}, key={}", topic, key, e);
        }
    }
}
