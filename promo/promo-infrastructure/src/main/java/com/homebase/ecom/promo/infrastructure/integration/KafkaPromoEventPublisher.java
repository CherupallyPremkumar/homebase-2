package com.homebase.ecom.promo.infrastructure.integration;

import com.homebase.ecom.promo.model.Coupon;
import com.homebase.ecom.promo.port.PromoEventPublisherPort;
import org.chenile.pubsub.ChenilePub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

/**
 * Infrastructure adapter that publishes promo domain events to Kafka
 * via ChenilePub. Handles JSON construction, topic routing, and
 * publish-after-commit transaction safety.
 */
public class KafkaPromoEventPublisher implements PromoEventPublisherPort {

    private static final Logger log = LoggerFactory.getLogger(KafkaPromoEventPublisher.class);
    private static final String TOPIC = "promo.events";

    private final ChenilePub chenilePub;

    public KafkaPromoEventPublisher(ChenilePub chenilePub) {
        this.chenilePub = chenilePub;
    }

    @Override
    public void publishPromoActivated(Coupon coupon) {
        String body = String.format(
                "{\"eventType\":\"PROMO_ACTIVATED\",\"promoCode\":\"%s\",\"promoName\":\"%s\",\"discountType\":\"%s\",\"discountValue\":%s}",
                coupon.getCode(), coupon.getName(), coupon.getDiscountType(), coupon.getDiscountValue());
        publishAfterCommit(coupon.getCode(), body, "PROMO_ACTIVATED");
    }

    @Override
    public void publishPromoExpired(Coupon coupon) {
        String body = String.format(
                "{\"eventType\":\"PROMO_EXPIRED\",\"promoCode\":\"%s\",\"promoName\":\"%s\",\"usageCount\":%d,\"usageLimit\":%d}",
                coupon.getCode(), coupon.getName(),
                coupon.getUsageCount() != null ? coupon.getUsageCount() : 0,
                coupon.getUsageLimit() != null ? coupon.getUsageLimit() : 0);
        publishAfterCommit(coupon.getCode(), body, "PROMO_EXPIRED");
    }

    private void publishAfterCommit(String key, String body, String eventType) {
        Runnable publishAction = () -> {
            try {
                chenilePub.publish(TOPIC, body, Map.of("key", key, "eventType", eventType));
                log.info("Published {} event for promoCode={}", eventType, key);
            } catch (Exception e) {
                log.error("Failed to publish {} event for promoCode={}", eventType, key, e);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    publishAction.run();
                }
            });
        } else {
            publishAction.run();
        }
    }
}
