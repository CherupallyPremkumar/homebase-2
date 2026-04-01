package com.homebase.ecom.rulesengine.service.cache;

import com.homebase.ecom.rulesengine.domain.model.RuleSet;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * In-memory cache for active rule sets, keyed by targetModule+tenant.
 * Entries expire after a configurable TTL. PostSaveHooks invalidate on state changes.
 */
public class RuleSetCacheManager {
    private static final Logger log = LoggerFactory.getLogger(RuleSetCacheManager.class);

    private final long ttlMillis;
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public RuleSetCacheManager(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public List<RuleSet> get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }
        if (Instant.now().toEpochMilli() - entry.createdAt > ttlMillis) {
            cache.remove(key);
            log.debug("Cache entry expired for key: {}", key);
            return null;
        }
        log.debug("Cache hit for key: {}", key);
        return entry.ruleSets;
    }

    public void put(String key, List<RuleSet> ruleSets) {
        cache.put(key, new CacheEntry(ruleSets, Instant.now().toEpochMilli()));
        log.debug("Cached {} rule sets for key: {}", ruleSets.size(), key);
    }

    public void evict(String key) {
        cache.remove(key);
        log.info("Evicted cache entry for key: {}", key);
    }

    public void evictAll() {
        cache.clear();
        log.info("Evicted all cached rule sets");
    }

    public int size() {
        return cache.size();
    }

    public static String cacheKey(String targetModule, String tenant) {
        return targetModule + "::" + (tenant != null ? tenant : "_default_");
    }

    private record CacheEntry(List<RuleSet> ruleSets, long createdAt) {}
}
