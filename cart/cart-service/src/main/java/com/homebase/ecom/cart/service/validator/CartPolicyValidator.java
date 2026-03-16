package com.homebase.ecom.cart.service.validator;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.exception.CartLimitExceededException;
import com.homebase.ecom.cart.exception.QuantityLimitExceededException;
import org.chenile.cconfig.sdk.CconfigClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Central policy validator for the Cart bounded context.
 *
 * Two-layer validation:
 * 1. Cconfig thresholds: reads from org/chenile/config/cart.json
 * 2. Policy engine: delegates complex cross-cutting rules (optional)
 */
@Component
public class CartPolicyValidator {

    private static final Logger log = LoggerFactory.getLogger(CartPolicyValidator.class);

    @Autowired(required = false)
    private CconfigClient cconfigClient;

    private final ObjectMapper mapper = new ObjectMapper();

    // ═══════════════════════════════════════════════════════════════════
    // POLICY: Item count
    // ═══════════════════════════════════════════════════════════════════

    public void validateItemCount(Cart cart) {
        JsonNode config = getCartConfig();
        int maxItems = configInt(config, "/policies/limits/maxItemsPerCart", 30);
        if (cart.getItems().size() >= maxItems) {
            throw new CartLimitExceededException(
                    "Cart item limit exceeded. Maximum allowed unique items: " + maxItems);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // POLICY: Quantity per item
    // ═══════════════════════════════════════════════════════════════════

    public void validateQuantity(int quantity) {
        JsonNode config = getCartConfig();
        int maxQty = configInt(config, "/policies/limits/maxQuantityPerItem", 10);
        if (quantity > maxQty) {
            throw new QuantityLimitExceededException(
                    "Quantity limit exceeded. Maximum allowed per item: " + maxQty);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // POLICY: Coupon count
    // ═══════════════════════════════════════════════════════════════════

    public void validateCouponCount(Cart cart) {
        JsonNode config = getCartConfig();
        int maxCoupons = configInt(config, "/policies/limits/maxCouponsPerCart", 3);
        if (cart.getCouponCodes().size() >= maxCoupons) {
            throw new IllegalArgumentException(
                    "Coupon limit exceeded. Maximum coupons per cart: " + maxCoupons);
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // POLICY: Minimum checkout amount (in smallest currency unit)
    // ═══════════════════════════════════════════════════════════════════

    public long getMinCheckoutAmount() {
        JsonNode config = getCartConfig();
        JsonNode node = config.at("/policies/limits/minCheckoutAmount");
        return (!node.isMissingNode() && node.isNumber()) ? node.asLong() : 100L;
    }

    // ═══════════════════════════════════════════════════════════════════
    // POLICY: Max cart value (in smallest currency unit)
    // ═══════════════════════════════════════════════════════════════════

    public void validateCartValue(Cart cart) {
        if (cart.getTotal() == null) return;
        JsonNode config = getCartConfig();
        JsonNode node = config.at("/policies/limits/maxCartValue");
        long maxValue = (!node.isMissingNode() && node.isNumber()) ? node.asLong() : 10_000_000L;
        if (cart.getTotal().getAmount() > maxValue) {
            throw new IllegalStateException(
                    "Cart value " + cart.getTotal().toDisplayString()
                    + " exceeds maximum allowed value of " + maxValue + " " + cart.getCurrency());
        }
    }

    // ═══════════════════════════════════════════════════════════════════
    // RULES: Expiration
    // ═══════════════════════════════════════════════════════════════════

    public int getCartExpirationHours() {
        JsonNode config = getCartConfig();
        return configInt(config, "/policies/expiration/cartExpirationHours", 72);
    }

    public int getAbandonmentThresholdHours() {
        JsonNode config = getCartConfig();
        return configInt(config, "/policies/expiration/abandonmentThresholdHours", 24);
    }

    public int getCheckoutReservationMinutes() {
        JsonNode config = getCartConfig();
        return configInt(config, "/policies/expiration/checkoutReservationMinutes", 15);
    }

    // ═══════════════════════════════════════════════════════════════════
    // RULES: Checkout
    // ═══════════════════════════════════════════════════════════════════

    public boolean isGuestCheckoutAllowed() {
        JsonNode config = getCartConfig();
        JsonNode node = config.at("/policies/checkout/allowGuestCheckout");
        return node.isMissingNode() || !node.isBoolean() || node.asBoolean();
    }

    // ═══════════════════════════════════════════════════════════════════
    // INTERNAL: Config helpers
    // ═══════════════════════════════════════════════════════════════════

    private JsonNode getCartConfig() {
        try {
            if (cconfigClient != null) {
                Map<String, Object> map = cconfigClient.value("cart", null);
                if (map != null) {
                    return mapper.valueToTree(map);
                }
            }
        } catch (Exception e) {
            log.warn("Failed to load cart.json from cconfig, using defaults: {}", e.getMessage());
        }
        return mapper.createObjectNode();
    }

    private int configInt(JsonNode config, String path, int defaultVal) {
        JsonNode node = config.at(path);
        return (!node.isMissingNode() && node.isInt()) ? node.asInt() : defaultVal;
    }
}
