package com.homebase.ecom.checkout.event;

import java.time.LocalDateTime;

/**
 * Published when payment fails during checkout.
 * Downstream: analytics, notification (alert customer).
 */
public class CheckoutPaymentFailedEvent extends CheckoutEvent {

    public static final String EVENT_TYPE = "CHECKOUT_PAYMENT_FAILED";

    private final String cartId;
    private final String failureReason;

    public CheckoutPaymentFailedEvent(String checkoutId, String cartId,
            String failureReason, LocalDateTime timestamp) {
        super(EVENT_TYPE, checkoutId, timestamp);
        this.cartId = cartId;
        this.failureReason = failureReason;
    }

    public String getCartId() { return cartId; }
    public String getFailureReason() { return failureReason; }
}
