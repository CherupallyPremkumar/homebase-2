package com.homebase.ecom.promo.port;

import com.homebase.ecom.promo.model.Coupon;

/**
 * Domain port for publishing promo lifecycle events.
 * Infrastructure adapter handles serialization and Kafka topic routing.
 */
public interface PromoEventPublisherPort {

    /**
     * Publishes an event when a promo becomes active.
     * Downstream: marketing notifications, cart BC applies promo availability.
     */
    void publishPromoActivated(Coupon coupon);

    /**
     * Publishes an event when a promo expires.
     * Downstream: cart BC removes active applications of this promo.
     */
    void publishPromoExpired(Coupon coupon);
}
