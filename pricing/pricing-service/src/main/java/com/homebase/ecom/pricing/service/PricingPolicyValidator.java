package com.homebase.ecom.pricing.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Reads pricing business rules from cconfig at runtime.
 * Same pattern as CartPolicyValidator, PromoPolicyValidator.
 * All values have sensible defaults for graceful degradation.
 */
public class PricingPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(PricingPolicyValidator.class);
    private static final String MODULE = "pricing";

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value(MODULE, null);
                if (map != null) return mapper.valueToTree(map);
            }
        } catch (Exception e) {
            log.warn("Failed to load pricing config, using defaults", e);
        }
        return mapper.createObjectNode();
    }

    public int getMaxDiscountPercentage() {
        JsonNode node = getConfig().at("/policies/discount/maxDiscountPercentage");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 50;
    }

    public int getMaxCouponsPerCart() {
        JsonNode node = getConfig().at("/policies/discount/maxCouponsPerCart");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 1;
    }

    /**
     * Returns the volume discount percentage for the given quantity.
     * Walks the volumeDiscountTiers array from highest to lowest, returns first match.
     */
    public int getVolumeDiscountPercent(int quantity) {
        JsonNode tiers = getConfig().at("/policies/volumeDiscountTiers");
        if (tiers.isMissingNode() || !tiers.isArray()) {
            // Default tiers
            if (quantity >= 10) return 20;
            if (quantity >= 3) return 10;
            return 0;
        }

        int bestPercent = 0;
        for (JsonNode tier : tiers) {
            int minQty = tier.path("minQty").asInt(0);
            int pct = tier.path("discountPct").asInt(0);
            if (quantity >= minQty && pct > bestPercent) {
                bestPercent = pct;
            }
        }
        return bestPercent;
    }

    public int getCustomerTierDiscount(String tier) {
        JsonNode node = getConfig().at("/policies/customerTierDiscounts/" + tier);
        if (!node.isMissingNode() && node.isInt()) return node.asInt();

        // Defaults
        return switch (tier) {
            case "VIP" -> 5;
            case "WHOLESALE" -> 15;
            case "PREMIUM" -> 8;
            default -> 0;
        };
    }

    public int getDefaultGstRate() {
        JsonNode node = getConfig().at("/policies/tax/defaultGstRate");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 18;
    }

    public int getGstRate(String category) {
        if (category == null) return getDefaultGstRate();
        JsonNode node = getConfig().at("/policies/tax/gstRateByCategory/" + category);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : getDefaultGstRate();
    }

    public int getPriceLockTTLMinutes() {
        JsonNode node = getConfig().at("/policies/priceLock/ttlMinutes");
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : 15;
    }

    public String getDefaultCurrency() {
        JsonNode node = getConfig().at("/policies/currency/defaultCurrency");
        return (!node.isMissingNode() && node.isTextual()) ? node.asText() : "INR";
    }

}
