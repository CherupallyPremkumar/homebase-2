package com.homebase.ecom.shipping.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * Central component for enforcing shipping policies and reading rule configurations.
 *
 * <p>Reads configurable rules from {@code org/chenile/config/shipping.json}
 * (overridable per tenant in DB via cconfig).
 */
@Component
public class ShippingPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(ShippingPolicyValidator.class);
    private static final Set<String> VALID_SHIPPING_METHODS = Set.of("STANDARD", "EXPRESS", "OVERNIGHT");

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Delivery Attempts
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that current delivery attempts have not exceeded the maximum.
     * Called before scheduling a retry.
     */
    public void validateDeliveryAttemptLimit(int currentAttempts) {
        int maxAttempts = getMaxDeliveryAttempts();
        if (currentAttempts >= maxAttempts) {
            throw new IllegalArgumentException(
                    "Maximum delivery attempts (" + maxAttempts + ") exceeded. Current: " + currentAttempts);
        }
    }

    public int getMaxDeliveryAttempts() {
        JsonNode config = getShippingConfig();
        return configInt(config, "/policies/delivery/maxDeliveryAttempts", 3);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // POLICY: Shipping Method Validation
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Validates that the shipping method is one of STANDARD, EXPRESS, OVERNIGHT.
     */
    public void validateShippingMethod(String method) {
        if (method == null || !VALID_SHIPPING_METHODS.contains(method.toUpperCase())) {
            throw new IllegalArgumentException(
                    "Invalid shipping method: " + method + ". Must be one of: " + VALID_SHIPPING_METHODS);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Delivery Days per Method
    // ═══════════════════════════════════════════════════════════════════════

    /**
     * Returns the estimated delivery days for a given shipping method.
     */
    public int getDeliveryDaysForMethod(String shippingMethod) {
        JsonNode config = getShippingConfig();
        if (shippingMethod == null) {
            return configInt(config, "/rules/delivery/standardShippingDays", 5);
        }
        switch (shippingMethod.toUpperCase()) {
            case "EXPRESS":
                return configInt(config, "/rules/delivery/expressShippingDays", 2);
            case "OVERNIGHT":
                return 1;
            case "STANDARD":
            default:
                return configInt(config, "/rules/delivery/standardShippingDays", 5);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RULES: Free Shipping & Return Window
    // ═══════════════════════════════════════════════════════════════════════

    public int getFreeShippingThreshold() {
        JsonNode config = getShippingConfig();
        return configInt(config, "/rules/shipping/freeShippingThreshold", 999);
    }

    public int getReturnWindowDays() {
        JsonNode config = getShippingConfig();
        return configInt(config, "/rules/returns/returnWindowDays", 30);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════════

    private JsonNode getShippingConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("shipping", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load shipping.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }
}
