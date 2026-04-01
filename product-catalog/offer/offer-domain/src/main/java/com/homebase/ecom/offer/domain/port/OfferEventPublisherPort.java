package com.homebase.ecom.offer.domain.port;

import com.homebase.ecom.offer.domain.model.Offer;

/**
 * Domain port for publishing offer lifecycle events.
 * Infrastructure adapter handles serialization, topics, and delivery mechanism.
 */
public interface OfferEventPublisherPort {

    /**
     * Publishes OFFER_LIVE event when offer goes live.
     * Consumed by catalog/pricing services to update product pricing.
     */
    void publishOfferLive(Offer offer);

    /**
     * Publishes OFFER_EXPIRED event when offer expires.
     * Consumed by catalog/pricing services to revert pricing.
     */
    void publishOfferExpired(Offer offer);

    /**
     * Publishes OFFER_SUSPENDED event when offer is suspended.
     * Consumed by catalog/pricing services to revert pricing.
     */
    void publishOfferSuspended(Offer offer, String reason);
}
