package com.homebase.ecom.checkout.event;

import java.time.LocalDateTime;

/**
 * Published when checkout is cancelled by customer or system.
 * Downstream: cart.cancelCheckout (restores cart to ACTIVE).
 */
public class CheckoutCancelledEvent extends CheckoutEvent {

    public static final String EVENT_TYPE = "CHECKOUT_CANCELLED";

    private final String cartId;
    private final String reason;

    public CheckoutCancelledEvent(String checkoutId, String cartId,
            String reason, LocalDateTime timestamp) {
        super(EVENT_TYPE, checkoutId, timestamp);
        this.cartId = cartId;
        this.reason = reason;
    }

    public String getCartId() { return cartId; }
    public String getReason() { return reason; }
}
