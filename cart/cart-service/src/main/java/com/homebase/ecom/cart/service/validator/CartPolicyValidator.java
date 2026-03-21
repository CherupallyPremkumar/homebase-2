package com.homebase.ecom.cart.service.validator;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.exception.CartLimitExceededException;
import com.homebase.ecom.cart.exception.QuantityLimitExceededException;
import com.homebase.ecom.cart.port.ConfigPort;

/**
 * Central policy validator for the Cart bounded context.
 * Reads thresholds from ConfigPort (domain port). No infrastructure dependency.
 */
public class CartPolicyValidator {

    private final ConfigPort configPort;

    public CartPolicyValidator(ConfigPort configPort) {
        this.configPort = configPort;
    }

    public void validateItemCount(Cart cart) {
        int maxItems = configPort.getMaxItemsPerCart();
        if (cart.getItems().size() >= maxItems) {
            throw new CartLimitExceededException(
                    "Cart item limit exceeded. Maximum allowed unique items: " + maxItems);
        }
    }

    public void validateQuantity(int quantity) {
        int maxQty = configPort.getMaxQuantityPerItem();
        if (quantity > maxQty) {
            throw new QuantityLimitExceededException(
                    "Quantity limit exceeded. Maximum allowed per item: " + maxQty);
        }
    }

    public void validateCouponCount(Cart cart) {
        int maxCoupons = configPort.getMaxCouponsPerCart();
        if (cart.getCouponCodes().size() >= maxCoupons) {
            throw new IllegalArgumentException(
                    "Coupon limit exceeded. Maximum coupons per cart: " + maxCoupons);
        }
    }

    public long getMinCheckoutAmount() {
        return configPort.getMinCheckoutAmount();
    }

    public void validateCartValue(Cart cart) {
        if (cart.getTotal() == null) return;
        long maxValue = configPort.getMaxCartValue();
        if (cart.getTotal().getAmount() > maxValue) {
            throw new IllegalStateException(
                    "Cart value " + cart.getTotal().toDisplayString()
                    + " exceeds maximum allowed value of " + maxValue + " " + cart.getCurrency());
        }
    }

    public int getCartExpirationHours() {
        return configPort.getCartExpirationHours();
    }

    public int getAbandonmentThresholdHours() {
        return configPort.getAbandonmentThresholdHours();
    }

    public int getCheckoutReservationMinutes() {
        return configPort.getCheckoutReservationMinutes();
    }

    public boolean isGuestCheckoutAllowed() {
        return configPort.isGuestCheckoutAllowed();
    }
}
