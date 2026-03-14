package com.homebase.ecom.pricing.infrastructure.cache;

import com.homebase.ecom.pricing.domain.model.LockedPriceBreakdown;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class PriceLockCacheManager {
    
    private static final String LOCK_PREFIX = "price_lock:";
    private final RedisTemplate<String, LockedPriceBreakdown> redisTemplate;

    public PriceLockCacheManager(RedisTemplate<String, LockedPriceBreakdown> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeLock(LockedPriceBreakdown lock) {
        String key = LOCK_PREFIX + lock.getLockToken();
        redisTemplate.opsForValue().set(key, lock, lock.getLockDurationMinutes(), TimeUnit.MINUTES);
    }

    public Optional<LockedPriceBreakdown> getLock(String lockToken) {
        String key = LOCK_PREFIX + lockToken;
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public boolean isLockValid(String lockToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOCK_PREFIX + lockToken));
    }

    public void invalidateLock(String lockToken) {
        redisTemplate.delete(LOCK_PREFIX + lockToken);
    }
}
