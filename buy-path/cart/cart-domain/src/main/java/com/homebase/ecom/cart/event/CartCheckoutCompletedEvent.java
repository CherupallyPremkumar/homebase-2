package com.homebase.ecom.cart.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Published when a cart checkout completes.
 * Downstream: order creation, analytics.
 */
public class CartCheckoutCompletedEvent extends CartEvent {

    public static final String EVENT_TYPE = "CART_CHECKOUT_COMPLETED";

    private final String userId;
    private final long totalAmount;
    private final String currency;
    private final List<CartItemPayload> items;
    private final String paymentId;
    private final String paymentGateway;
    private final String paymentTransactionId;
    private final String promoCode;
    private final long discountAmount;

    public CartCheckoutCompletedEvent(String cartId, String userId, long totalAmount,
            String currency, List<CartItemPayload> items, String paymentId,
            String paymentGateway, String paymentTransactionId,
            String promoCode, long discountAmount, LocalDateTime timestamp) {
        super(EVENT_TYPE, cartId, timestamp);
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.items = items;
        this.paymentId = paymentId;
        this.paymentGateway = paymentGateway;
        this.paymentTransactionId = paymentTransactionId;
        this.promoCode = promoCode;
        this.discountAmount = discountAmount;
    }

    public String getUserId() { return userId; }
    public long getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public List<CartItemPayload> getItems() { return items; }
    public String getPaymentId() { return paymentId; }
    public String getPaymentGateway() { return paymentGateway; }
    public String getPaymentTransactionId() { return paymentTransactionId; }
    public String getPromoCode() { return promoCode; }
    public long getDiscountAmount() { return discountAmount; }
}
