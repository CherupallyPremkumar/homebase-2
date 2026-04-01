package com.homebase.ecom.cart.jobs.translator;

import com.homebase.ecom.cart.jobs.query.CartQueryPort;
import com.homebase.ecom.shared.event.CouponExpiredEvent;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

/**
 * Translates COUPON_EXPIRED from promo.events into expireCoupon cart events.
 *
 * promo.events/COUPON_EXPIRED {couponCode}
 *   → query cart-query: findActiveCartsWithCoupon(couponCode)
 *   → fan-out: publish {cartId, "expireCoupon", {couponCode}} to cart.events
 */
public class CouponExpiredTranslator extends AbstractCartEventTranslator {

    private final CartQueryPort cartQueryPort;

    public CouponExpiredTranslator(ChenilePub chenilePub, CartQueryPort cartQueryPort) {
        super(chenilePub);
        this.cartQueryPort = cartQueryPort;
    }

    @KafkaListener(topics = KafkaTopics.PROMO_EVENTS, groupId = "cart-jobs",
            containerFactory = "kafkaListenerContainerFactory")
    public void onCouponExpired(CouponExpiredEvent event) {
        if (event.getCouponCode() == null) {
            log.warn("CouponExpiredEvent missing couponCode, skipping");
            return;
        }

        log.info("Coupon expired: {}", event.getCouponCode());

        List<String> cartIds = cartQueryPort.findActiveCartsWithCoupon(event.getCouponCode());

        String payload = "{\"couponCode\":\"" + event.getCouponCode() + "\"}";
        fanOut(cartIds, "expireCoupon", payload);
    }
}
