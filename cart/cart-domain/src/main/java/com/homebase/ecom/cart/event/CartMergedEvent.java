package com.homebase.ecom.cart.event;

import java.time.LocalDateTime;

/**
 * Published when a guest cart is merged into an authenticated user's cart.
 * Downstream: analytics, inventory releases source cart reservations.
 */
public class CartMergedEvent extends CartEvent {

    public static final String EVENT_TYPE = "CART_MERGED";

    private final String targetCartId;
    private final String customerId;

    public CartMergedEvent(String sourceCartId, String targetCartId, String customerId,
            LocalDateTime timestamp) {
        super(EVENT_TYPE, sourceCartId, timestamp);
        this.targetCartId = targetCartId;
        this.customerId = customerId;
    }

    public String getTargetCartId() { return targetCartId; }
    public String getCustomerId() { return customerId; }
}
