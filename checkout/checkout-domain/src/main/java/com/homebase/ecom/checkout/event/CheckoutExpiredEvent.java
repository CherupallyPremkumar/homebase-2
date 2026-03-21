package com.homebase.ecom.checkout.event;

import java.time.LocalDateTime;

/**
 * Published when checkout expires due to timeout (default 30 min).
 * Downstream: cart restoration, inventory release.
 */
public class CheckoutExpiredEvent extends CheckoutEvent {

    public static final String EVENT_TYPE = "CHECKOUT_EXPIRED";

    private final String cartId;

    public CheckoutExpiredEvent(String checkoutId, String cartId, LocalDateTime timestamp) {
        super(EVENT_TYPE, checkoutId, timestamp);
        this.cartId = cartId;
    }

    public String getCartId() { return cartId; }
}
