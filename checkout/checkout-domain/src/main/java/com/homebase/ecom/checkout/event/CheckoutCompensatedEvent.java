package com.homebase.ecom.checkout.event;

import java.time.LocalDateTime;

/**
 * Published when all checkout saga steps are compensated (rolled back).
 * Downstream: cart.cancelCheckout (restores cart to ACTIVE).
 */
public class CheckoutCompensatedEvent extends CheckoutEvent {

    public static final String EVENT_TYPE = "CHECKOUT_COMPENSATED";

    private final String cartId;
    private final String reason;

    public CheckoutCompensatedEvent(String checkoutId, String cartId,
            String reason, LocalDateTime timestamp) {
        super(EVENT_TYPE, checkoutId, timestamp);
        this.cartId = cartId;
        this.reason = reason;
    }

    public String getCartId() { return cartId; }
    public String getReason() { return reason; }
}
