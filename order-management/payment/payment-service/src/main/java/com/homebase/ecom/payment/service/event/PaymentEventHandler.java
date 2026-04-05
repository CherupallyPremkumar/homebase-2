package com.homebase.ecom.payment.service.event;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.payment.domain.model.Payment;
import com.homebase.ecom.payment.dto.InitiatePaymentPayload;
import com.homebase.ecom.payment.infrastructure.persistence.adapter.PaymentQueryAdapter;
import com.homebase.ecom.shared.event.*;
import org.chenile.pubsub.ChenilePub;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Chenile event handler for payment cross-service events.
 * Registered via paymentEventService.json -- operations subscribe to Kafka topics
 * through chenile-kafka auto-subscription (CustomKafkaConsumer + EventProcessor).
 *
 * CONSUMES order.events: ORDER_CREATED -> initiate payment
 * PUBLISHES to payment.events: PAYMENT_SUCCEEDED, PAYMENT_FAILED, PAYMENT_REFUNDED (via PostSaveHooks)
 *
 * Bean name "paymentEventService" -- must match the service JSON id.
 */
public class PaymentEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventHandler.class);

    private final PaymentQueryAdapter paymentQueryAdapter;
    private final StateEntityServiceImpl<Payment> paymentStateEntityService;
    private final ChenilePub chenilePub;
    private final ObjectMapper objectMapper;

    public PaymentEventHandler(
            PaymentQueryAdapter paymentQueryAdapter,
            @Qualifier("_paymentStateEntityService_") StateEntityServiceImpl<Payment> paymentStateEntityService,
            ChenilePub chenilePub,
            ObjectMapper objectMapper) {
        this.paymentQueryAdapter = paymentQueryAdapter;
        this.paymentStateEntityService = paymentStateEntityService;
        this.chenilePub = chenilePub;
        this.objectMapper = objectMapper;
    }

    // ── order.events ──────────────────────────────────────────────────────

    @Transactional
    public void handleOrderEvent(OrderCreatedEvent event) {
        if (!OrderCreatedEvent.EVENT_TYPE.equals(event.getEventType())) {
            return;
        }
        log.info("Payment: received OrderCreatedEvent for order: {}", event.getOrderId());

        // Idempotency check
        if (paymentQueryAdapter.findByOrderId(event.getOrderId()).isPresent()) {
            log.warn("Idempotency: payment already exists for order {} (possible replay). Skipping.",
                    event.getOrderId());
            return;
        }

        try {
            // Create a new Payment through STM — sets initial state (INITIATED) and persists via EntityStore
            Payment payment = new Payment();
            payment.setOrderId(event.getOrderId());
            payment.setCustomerId(event.getCustomerId());
            payment.setAmount(event.getTotalAmount());
            payment.setCurrency("INR");

            paymentStateEntityService.process(payment, null, null);

            log.info("Payment initiated for order: {}, paymentId: {}", event.getOrderId(), payment.getId());
        } catch (RuntimeException e) {
            log.warn("Idempotency: payment initiation for order {} already completed (possible replay). Skipping. Detail: {}",
                    event.getOrderId(), e.getMessage());
        } catch (Exception e) {
            log.error("Failed to initiate payment for order: {}", event.getOrderId(), e);
        }
    }

    // ── Publishing helpers ────────────────────────────────────────────────

    private void publish(String topic, String key, Object payload) {
        try {
            String body = objectMapper.writeValueAsString(payload);
            java.util.Map<String, Object> headers = new java.util.HashMap<>();
            headers.put("key", key);
            chenilePub.publish(topic, body, headers);
        } catch (JacksonException e) {
            log.error("Failed to serialize event for topic={}, key={}", topic, key, e);
        }
    }
}
