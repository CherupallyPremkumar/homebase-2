package com.homebase.ecom.pricing.domain.port;

import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;

import java.util.Optional;

/**
 * Port for price-lock storage (Redis-backed).
 * Locks a calculated PriceBreakdown for a TTL so checkout can proceed
 * with guaranteed pricing.
 */
public interface PriceLockPort {
    void store(LockedPriceBreakdown lock);
    Optional<LockedPriceBreakdown> retrieve(String lockToken);
    boolean isValid(String lockToken);
    void invalidate(String lockToken);
}
