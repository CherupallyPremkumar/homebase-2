package com.homebase.ecom.cart.port;

/**
 * Domain port for cart policy configuration.
 * Infrastructure adapter reads values from cconfig; domain only sees typed methods.
 */
public interface ConfigPort {
    int getMaxItemsPerCart();
    int getMaxQuantityPerItem();
    int getMaxCouponsPerCart();
    long getMinCheckoutAmount();
    long getMaxCartValue();
    int getCartExpirationHours();
    int getAbandonmentThresholdHours();
    int getCheckoutReservationMinutes();
    boolean isGuestCheckoutAllowed();
}
