package com.homebase.ecom.offer.domain.port;

import java.math.BigDecimal;

/**
 * Port for communicating pricing updates to the Pricing BC.
 * When an offer goes live, we update catalog pricing.
 * When it expires, we revert to original pricing.
 */
public interface PricingPort {

    /**
     * Update catalog pricing when an offer goes live.
     */
    void updateOfferPricing(String productId, String offerId, BigDecimal offerPrice);

    /**
     * Revert catalog pricing when an offer expires or is suspended.
     */
    void revertOfferPricing(String productId, String offerId);
}
