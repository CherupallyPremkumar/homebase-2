package com.homebase.ecom.pricing.domain.port;

import com.homebase.ecom.shared.Money;
import java.util.Optional;

/**
 * Port for fetching authoritative prices from the Offer module.
 * In Amazon's model, Offer is the single source of truth for prices —
 * each seller sets a listingPrice and optional salePrice via their Offer.
 *
 * Pricing MUST fetch prices from here, never trust prices sent by the caller.
 */
public interface OfferPricePort {

    /**
     * Full offer price details for a variant+seller combination.
     */
    record OfferPrice(
            Money listingPrice,    // seller's regular price (always present)
            Money salePrice,       // promotional price if active (null if none)
            String offerId,        // the offer ID for audit trail
            boolean buyBoxWinner   // whether this is the winning offer
    ) {
        public Money effectivePrice() {
            return salePrice != null ? salePrice : listingPrice;
        }

        public boolean hasActivePromo() {
            return salePrice != null && salePrice.isLessThan(listingPrice);
        }
    }

    /**
     * Get the current offer price for a variant from a specific seller.
     * Returns empty if no active offer exists for this variant+seller.
     */
    Optional<OfferPrice> getOfferPrice(String variantId, String sellerId, String currency);
}
