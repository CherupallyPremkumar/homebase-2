package com.homebase.ecom.catalog.service;

import com.homebase.ecom.catalog.exception.*;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.cconfig.sdk.CconfigClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Domain service that validates catalog business rules using cconfig (static policies).
 */
public class CatalogPolicyValidator {

    private final CconfigClient cconfigClient;

    private static final String MODULE = "catalog";
    private static final int DEFAULT_MAX_FEATURED = 20;
    private static final int DEFAULT_MAX_DEPTH = 4;
    private static final Set<String> RESERVED_TAGS = Set.of("__system", "__internal");

    public CatalogPolicyValidator(CconfigClient cconfigClient) {
        this.cconfigClient = cconfigClient;
    }

    public void validateFeaturedItemCount(int currentFeaturedCount) {
        int max = getIntConfig("catalog.maxFeaturedItems", DEFAULT_MAX_FEATURED);
        if (currentFeaturedCount >= max) {
            throw new MaxFeaturedItemsExceededException(currentFeaturedCount, max);
        }
    }

    public void validateCatalogItemTags(List<String> tags) {
        if (tags == null) return;
        for (String tag : tags) {
            if (RESERVED_TAGS.contains(tag)) {
                throw new InvalidCatalogTagException(tag);
            }
        }
    }

    public void validateCategoryDepth(int level) {
        int maxDepth = getIntConfig("catalog.maxCategoryDepth", DEFAULT_MAX_DEPTH);
        if (level > maxDepth) {
            throw new CategoryDepthLimitExceededException(level, maxDepth);
        }
    }

    public void validateFeaturedCollectionImage(Collection collection) {
        if (Boolean.TRUE.equals(collection.getFeatured())
                && (collection.getImageUrl() == null || collection.getImageUrl().isBlank())) {
            throw new FeaturedCollectionImageRequiredException(collection.getName());
        }
    }

    public void validateFeaturedCollectionCount(int currentCount) {
        int max = getIntConfig("catalog.maxFeaturedCollections", 10);
        if (currentCount >= max) {
            throw new MaxFeaturedCollectionsExceededException(currentCount, max);
        }
    }

    public boolean shouldHideOutOfStockItems() {
        return getBoolConfig("catalog.hideOutOfStock", true);
    }

    public boolean shouldAutoReactivateOnRestock() {
        return getBoolConfig("catalog.autoReactivateOnRestock", true);
    }

    private int getIntConfig(String key, int defaultValue) {
        try {
            Map<String, Object> config = cconfigClient.value(MODULE, key);
            if (config != null && config.containsKey("value")) {
                return Integer.parseInt(config.get("value").toString());
            }
        } catch (Exception ignored) {}
        return defaultValue;
    }

    private boolean getBoolConfig(String key, boolean defaultValue) {
        try {
            Map<String, Object> config = cconfigClient.value(MODULE, key);
            if (config != null && config.containsKey("value")) {
                return Boolean.parseBoolean(config.get("value").toString());
            }
        } catch (Exception ignored) {}
        return defaultValue;
    }
}
