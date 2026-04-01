package com.homebase.ecom.cart.event;

import java.time.LocalDateTime;

/**
 * Published when a cart is abandoned.
 * Downstream: inventory releases early reservations.
 */
public class CartAbandonedEvent extends CartEvent {

    public static final String EVENT_TYPE = "CART_ABANDONED";

    public CartAbandonedEvent(String cartId, LocalDateTime timestamp) {
        super(EVENT_TYPE, cartId, timestamp);
    }
}
