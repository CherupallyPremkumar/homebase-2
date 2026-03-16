package com.homebase.ecom.offer.infrastructure.adapter;

import com.homebase.ecom.offer.domain.port.PricingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Infrastructure adapter for Pricing BC integration.
 * In production, this would call the Pricing service REST API or publish events.
 * Currently logs the intent for downstream implementation.
 */
public class PricingAdapter implements PricingPort {

    private static final Logger log = LoggerFactory.getLogger(PricingAdapter.class);

    @Override
    public void updateOfferPricing(String productId, String offerId, BigDecimal offerPrice) {
        log.info("PricingPort: Updating catalog pricing for product={}, offer={}, offerPrice={}",
                productId, offerId, offerPrice);
    }

    @Override
    public void revertOfferPricing(String productId, String offerId) {
        log.info("PricingPort: Reverting catalog pricing for product={}, offer={}",
                productId, offerId);
    }
}
