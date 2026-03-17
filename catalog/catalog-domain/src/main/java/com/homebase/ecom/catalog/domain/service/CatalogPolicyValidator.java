package com.homebase.ecom.catalog.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.catalog.exception.*;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Domain service for enforcing catalog policies.
 * Moved to domain layer to represent business invariants.
 */
public class CatalogPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(CatalogPolicyValidator.class);
    private final CconfigClient cconfigClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public CatalogPolicyValidator(CconfigClient cconfigClient) {
        this.cconfigClient = cconfigClient;
    }

    private JsonNode getCatalogConfig() {
        try {
            Map<String, Object> map = cconfigClient.value("catalog", null);
            if (map != null) {
                return mapper.valueToTree(map);
            }
        } catch (Exception e) {
            log.warn("Failed to load catalog.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    public void validateFeaturedItemCount(int currentFeaturedCount) {
        JsonNode config = getCatalogConfig();
        int max = 20;
        JsonNode node = config.at("/policies/catalogItem/maxFeaturedItems");
        if (!node.isMissingNode() && node.isInt()) {
            max = node.asInt();
        }
        if (currentFeaturedCount >= max) {
            throw new MaxFeaturedItemsExceededException(currentFeaturedCount, max);
        }
    }

    public void validateCatalogItemTags(List<String> tags) {
        if (tags == null || tags.isEmpty())
            return;
        JsonNode config = getCatalogConfig();

        int maxTags = 10;
        JsonNode maxTagsNode = config.at("/policies/catalogItem/maxTagsPerItem");
        if (!maxTagsNode.isMissingNode() && maxTagsNode.isInt()) {
            maxTags = maxTagsNode.asInt();
        }
        if (tags.size() > maxTags) {
            throw new IllegalArgumentException(
                    "CatalogItem has " + tags.size() + " tags but the maximum allowed is " + maxTags + ".");
        }

        List<String> allowedTags = getAllowedTags(config);
        if (!allowedTags.isEmpty()) {
            for (String tag : tags) {
                if (!allowedTags.contains(tag.toLowerCase())) {
                    throw new InvalidCatalogTagException(tag);
                }
            }
        }
    }

    private List<String> getAllowedTags(JsonNode config) {
        List<String> allowed = new ArrayList<>();
        JsonNode node = config.at("/policies/catalogItem/allowedTags");
        if (!node.isMissingNode() && node.isArray()) {
            node.forEach(t -> allowed.add(t.asText().toLowerCase()));
        }
        return allowed;
    }

    public void validateCategoryDepth(int newLevel) {
        JsonNode config = getCatalogConfig();
        int maxDepth = 3;
        JsonNode node = config.at("/policies/category/maxDepthLevels");
        if (!node.isMissingNode() && node.isInt()) {
            maxDepth = node.asInt();
        }
        if (newLevel >= maxDepth) {
            throw new CategoryDepthLimitExceededException(newLevel, maxDepth);
        }
    }

    public void validateFeaturedCollectionImage(Collection collection) {
        JsonNode config = getCatalogConfig();
        JsonNode required = config.at("/policies/collection/requireImageForFeaturedCollection");
        if (required.isMissingNode() || required.asBoolean(true)) {
            if (Boolean.TRUE.equals(collection.getFeatured())
                    && (collection.getImageUrl() == null || collection.getImageUrl().trim().isEmpty())) {
                throw new FeaturedCollectionImageRequiredException(collection.getName());
            }
        }
    }

    public void validateFeaturedCollectionCount(int currentFeaturedCount) {
        JsonNode config = getCatalogConfig();
        int max = 5;
        JsonNode node = config.at("/policies/collection/maxFeaturedCollections");
        if (!node.isMissingNode() && node.isInt()) {
            max = node.asInt();
        }
        if (currentFeaturedCount >= max) {
            throw new MaxFeaturedCollectionsExceededException(currentFeaturedCount, max);
        }
    }

    public boolean shouldHideOutOfStockItems() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/visibility/hideOutOfStockItems");
        return node.isMissingNode() || node.asBoolean(true);
    }

    public boolean shouldAutoReactivateOnRestock() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/policies/catalogItem/autoReactivateOnRestock");
        return node.isMissingNode() || node.asBoolean(true);
    }

    public int getLowStockThreshold() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/visibility/lowStockThreshold");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 5;
    }

    public int getDynamicCollectionBatchSizeLimit() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/collection/dynamicCollectionBatchSizeLimit");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 500;
    }

    public int getStaleDynamicCollectionRefreshHours() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/collection/staleDynamicCollectionRefreshHours");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 24;
    }

    public int getDefaultPageSize() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/search/defaultPageSize");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 20;
    }

    public int getMaxPageSize() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/search/maxPageSize");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 100;
    }

    public int getNewArrivalWindowDays() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/merchandising/newArrivalWindowDays");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 30;
    }

    public int getBestsellersMinOrderCount() {
        JsonNode config = getCatalogConfig();
        JsonNode node = config.at("/rules/merchandising/bestsellersMinOrderCount");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 10;
    }
}
