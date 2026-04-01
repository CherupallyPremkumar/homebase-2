package com.homebase.ecom.pricing.infrastructure.cache;

import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import com.homebase.ecom.pricing.domain.port.PriceLockPort;

import java.util.Optional;

/**
 * Infrastructure adapter: delegates to PriceLockCacheManager (Redis).
 */
public class PriceLockAdapter implements PriceLockPort {

    private final PriceLockCacheManager cacheManager;

    public PriceLockAdapter(PriceLockCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public void store(LockedPriceBreakdown lock) {
        cacheManager.storeLock(lock);
    }

    @Override
    public Optional<LockedPriceBreakdown> retrieve(String lockToken) {
        return cacheManager.getLock(lockToken);
    }

    @Override
    public boolean isValid(String lockToken) {
        return cacheManager.isLockValid(lockToken);
    }

    @Override
    public void invalidate(String lockToken) {
        cacheManager.invalidateLock(lockToken);
    }
}
