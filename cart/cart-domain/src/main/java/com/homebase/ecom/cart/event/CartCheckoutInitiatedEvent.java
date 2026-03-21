package com.homebase.ecom.cart.event;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Published when a cart checkout is initiated.
 * Downstream: checkout saga, inventory reservation.
 */
public class CartCheckoutInitiatedEvent extends CartEvent {

    public static final String EVENT_TYPE = "CART_CHECKOUT_INITIATED";

    private final String userId;
    private final long totalAmount;
    private final String currency;
    private final List<CartItemPayload> items;
    private final String promoCode;
    private final long discountAmount;

    public CartCheckoutInitiatedEvent(String cartId, String userId, long totalAmount,
            String currency, List<CartItemPayload> items,
            String promoCode, long discountAmount, LocalDateTime timestamp) {
        super(EVENT_TYPE, cartId, timestamp);
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.items = items;
        this.promoCode = promoCode;
        this.discountAmount = discountAmount;
    }

    public String getUserId() { return userId; }
    public long getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public List<CartItemPayload> getItems() { return items; }
    public String getPromoCode() { return promoCode; }
    public long getDiscountAmount() { return discountAmount; }
}
