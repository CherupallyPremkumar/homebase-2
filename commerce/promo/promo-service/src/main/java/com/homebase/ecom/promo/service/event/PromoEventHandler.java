package com.homebase.ecom.promo.service.event;

import com.homebase.ecom.promo.dto.IncrementUsagePayload;
import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.shared.event.EventEnvelope;
import com.homebase.ecom.shared.event.OrderCompletedEvent;
import org.chenile.workflow.service.impl.StateEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

/**
 * Item 10: Chenile-kafka event handler for promo cross-service events.
 *
 * CONSUMES order.events: ORDER_COMPLETED -> increment usage count on the promo
 * used in the completed order.
 *
 * Bean name "promoEventService" must match the service JSON id
 * so ChenileServiceInitializer can resolve the service reference.
 */
public class PromoEventHandler {

    private static final Logger log = LoggerFactory.getLogger(PromoEventHandler.class);

    private final StateEntityServiceImpl<Coupon> promoStateEntityService;
    private final ObjectMapper objectMapper;

    public PromoEventHandler(
            @Qualifier("_promoStateEntityService_") StateEntityServiceImpl<Coupon> promoStateEntityService,
            ObjectMapper objectMapper) {
        this.promoStateEntityService = promoStateEntityService;
        this.objectMapper = objectMapper;
    }

    /**
     * Handles order events. On ORDER_COMPLETED, increments usage count
     * for the promo code used in the order.
     */
    @Transactional
    public void handleOrderEvent(EventEnvelope envelope) {
        if (envelope == null || envelope.getEventType() == null) {
            return;
        }

        if ("ORDER_COMPLETED".equals(envelope.getEventType())) {
            try {
                OrderCompletedEvent event = objectMapper.convertValue(
                        envelope.getPayload(), OrderCompletedEvent.class);

                // The promo code ID should be available in the event payload or headers
                String promoId = extractPromoId(envelope);
                if (promoId == null) {
                    log.debug("No promo code associated with completed order: {}", event.getOrderId());
                    return;
                }

                log.info("Promo: received ORDER_COMPLETED for order: {}, incrementing usage for promo: {}",
                        event.getOrderId(), promoId);

                IncrementUsagePayload payload = new IncrementUsagePayload();
                payload.setOrderId(event.getOrderId());
                payload.setCustomerId(event.getCustomerId());
                payload.setComment("Usage incremented for completed order " + event.getOrderId());

                promoStateEntityService.processById(promoId, "incrementUsage", payload);

                log.info("Usage incremented for promo: {} from order: {}", promoId, event.getOrderId());
            } catch (IllegalStateException e) {
                log.warn("Idempotency: usage already incremented for order (possible replay). Skipping. Detail: {}",
                        e.getMessage());
            } catch (Exception e) {
                log.error("Failed to increment promo usage for order event: {}", envelope, e);
            }
        }
    }

    private String extractPromoId(EventEnvelope envelope) {
        // The promo ID can be embedded in event headers or payload
        if (envelope.getPayload() instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> payloadMap = (java.util.Map<String, Object>) envelope.getPayload();
            Object promoId = payloadMap.get("promoId");
            if (promoId != null) return promoId.toString();
        }
        return null;
    }
}
