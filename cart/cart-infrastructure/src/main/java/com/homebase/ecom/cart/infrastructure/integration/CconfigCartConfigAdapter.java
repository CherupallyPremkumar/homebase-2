package com.homebase.ecom.cart.infrastructure.integration;

import com.homebase.ecom.cart.port.ConfigPort;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Infrastructure adapter: reads cart policy config from cconfig service.
 * Returns typed values with sensible defaults if cconfig is unavailable.
 */
public class CconfigCartConfigAdapter implements ConfigPort {

    private static final Logger log = LoggerFactory.getLogger(CconfigCartConfigAdapter.class);

    private final CconfigClient cconfigClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public CconfigCartConfigAdapter(CconfigClient cconfigClient) {
        this.cconfigClient = cconfigClient;
    }

    @Override
    public int getMaxItemsPerCart() {
        return configInt("/policies/limits/maxItemsPerCart", 30);
    }

    @Override
    public int getMaxQuantityPerItem() {
        return configInt("/policies/limits/maxQuantityPerItem", 10);
    }

    @Override
    public int getMaxCouponsPerCart() {
        return configInt("/policies/limits/maxCouponsPerCart", 3);
    }

    @Override
    public long getMinCheckoutAmount() {
        return configLong("/policies/limits/minCheckoutAmount", 100L);
    }

    @Override
    public long getMaxCartValue() {
        return configLong("/policies/limits/maxCartValue", 10_000_000L);
    }

    @Override
    public int getCartExpirationHours() {
        return configInt("/policies/expiration/cartExpirationHours", 72);
    }

    @Override
    public int getAbandonmentThresholdHours() {
        return configInt("/policies/expiration/abandonmentThresholdHours", 24);
    }

    @Override
    public int getCheckoutReservationMinutes() {
        return configInt("/policies/expiration/checkoutReservationMinutes", 15);
    }

    @Override
    public boolean isGuestCheckoutAllowed() {
        JsonNode config = getCartConfig();
        JsonNode node = config.at("/policies/checkout/allowGuestCheckout");
        return node.isMissingNode() || !node.isBoolean() || node.asBoolean();
    }

    private JsonNode getCartConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("cart", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load cart config from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(String path, int defaultVal) {
        JsonNode node = getCartConfig().at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }

    private long configLong(String path, long defaultVal) {
        JsonNode node = getCartConfig().at(path);
        return (!node.isMissingNode() && node.isNumber()) ? node.asLong() : defaultVal;
    }
}
