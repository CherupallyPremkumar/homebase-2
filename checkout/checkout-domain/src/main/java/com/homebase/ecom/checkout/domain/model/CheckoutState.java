package com.homebase.ecom.checkout.domain.model;

/**
 * State Machine for Checkout
 * Defines valid state transitions
 */
public enum CheckoutState {
    INITIALIZED("Just created, not started"),
    CART_LOCKED("Cart is locked, can't modify"),
    PRICE_LOCKED("Price is guaranteed for 15 min"),
    ORDER_CREATED("Order record created in DB"),
    PAYMENT_INITIATED("Payment session created with Stripe"),
    PAYMENT_PENDING("Waiting for Stripe webhook"),
    PAYMENT_SUCCESS("Payment succeeded, order confirmed"),
    PAYMENT_FAILED("Payment failed, order abandoned"),
    COMPLETED("All steps done, user redirected"),
    ABANDONED("User cancelled or timeout"),
    CANCELLED("Explicitly cancelled by user"),
    COMPENSATION_STARTED("Rolling back transaction"),
    COMPENSATION_COMPLETED("Rollback finished");

    private final String description;

    CheckoutState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
