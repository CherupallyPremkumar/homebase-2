package com.homebase.ecom.pricing.infrastructure.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PricingEventListener {

    private static final Logger log = LoggerFactory.getLogger(PricingEventListener.class);

    @KafkaListener(topics = "cart-events", groupId = "pricing-service-group")
    public void onCartUpdated(CartUpdatedEvent event) {
        try {
            log.info("Received CartUpdatedEvent for cart: {}. Invalidating evaluation cache.", event.getCartId());
            // Logic to invalidate internal pricing caches for this cart
        } catch (Exception e) {
            log.warn("Idempotency: error processing CartUpdatedEvent for cart {} (possible replay). Skipping. Detail: {}",
                    event.getCartId(), e.getMessage());
        }
    }

    @KafkaListener(topics = "promo-events", groupId = "pricing-service-group")
    public void onCouponUsed(CouponUsedEvent event) {
        try {
            log.info("Received CouponUsedEvent for order: {}. Recording discount for reporting.", event.getOrderId());
            // Logic to record discount usage for financial reporting
        } catch (Exception e) {
            log.warn("Idempotency: error processing CouponUsedEvent for order {} (possible replay). Skipping. Detail: {}",
                    event.getOrderId(), e.getMessage());
        }
    }
}
