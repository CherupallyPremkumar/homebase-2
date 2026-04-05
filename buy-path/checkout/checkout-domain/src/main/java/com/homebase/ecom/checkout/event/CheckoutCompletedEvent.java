package com.homebase.ecom.checkout.event;

import java.time.LocalDateTime;

/**
 * Published when checkout completes successfully (payment succeeded).
 * Downstream: cart.completeCheckout, order.create, analytics.
 */
public class CheckoutCompletedEvent extends CheckoutEvent {

    public static final String EVENT_TYPE = "CHECKOUT_COMPLETED";

    private final String cartId;
    private final String orderId;
    private final String customerId;
    private final String paymentId;
    private final long totalAmount;
    private final String currency;

    public CheckoutCompletedEvent(String checkoutId, String cartId, String orderId,
            String customerId, String paymentId, long totalAmount,
            String currency, LocalDateTime timestamp) {
        super(EVENT_TYPE, checkoutId, timestamp);
        this.cartId = cartId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.paymentId = paymentId;
        this.totalAmount = totalAmount;
        this.currency = currency;
    }

    public String getCartId() { return cartId; }
    public String getOrderId() { return orderId; }
    public String getCustomerId() { return customerId; }
    public String getPaymentId() { return paymentId; }
    public long getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
}
