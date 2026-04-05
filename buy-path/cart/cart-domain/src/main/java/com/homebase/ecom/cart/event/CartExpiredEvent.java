package com.homebase.ecom.cart.event;

import java.time.LocalDateTime;

/**
 * Published when a cart expires.
 * Downstream: inventory releases reservations.
 */
public class CartExpiredEvent extends CartEvent {

    public static final String EVENT_TYPE = "CART_EXPIRED";

    private final String customerId;

    public CartExpiredEvent(String cartId, String customerId, LocalDateTime timestamp) {
        super(EVENT_TYPE, cartId, timestamp);
        this.customerId = customerId;
    }

    public String getCustomerId() { return customerId; }
}
