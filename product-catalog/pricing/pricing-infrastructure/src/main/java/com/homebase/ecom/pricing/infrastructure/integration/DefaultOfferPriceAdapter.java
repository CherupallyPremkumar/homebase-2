package com.homebase.ecom.pricing.infrastructure.integration;

import com.homebase.ecom.pricing.domain.port.OfferPricePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Infrastructure adapter: fetches offer prices for variant+seller combinations.
 * Currently returns empty — pricing falls back to cart snapshot basePrice.
 * Will be replaced with a real adapter calling offer-client when Offer module
 * is production-ready with buy-box and seller pricing.
 */
public class DefaultOfferPriceAdapter implements OfferPricePort {

    private static final Logger log = LoggerFactory.getLogger(DefaultOfferPriceAdapter.class);

    @Override
    public Optional<OfferPrice> getOfferPrice(String variantId, String sellerId, String currency) {
        log.debug("Offer price lookup for variantId={}, sellerId={}, currency={} — offer module not connected, returning empty",
                variantId, sellerId, currency);
        return Optional.empty();
    }
}
