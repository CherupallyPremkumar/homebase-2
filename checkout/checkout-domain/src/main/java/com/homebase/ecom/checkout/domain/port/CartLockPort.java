package com.homebase.ecom.checkout.domain.port;

import com.homebase.ecom.checkout.model.Checkout;

/**
 * Port for locking/unlocking the cart during checkout.
 * Adapter calls Cart service's initiateCheckout / cancelCheckout.
 */
public interface CartLockPort {

    /**
     * Locks the cart (transitions to CHECKOUT_INITIATED) and returns a snapshot.
     * @return cart snapshot with items, totals, coupon codes
     */
    CartSnapshot lockCart(String cartId);

    /**
     * Unlocks the cart (transitions back to ACTIVE) — used for compensation.
     */
    void unlockCart(String cartId);

    record CartSnapshot(
        String customerId,
        String currency,
        long subtotal,
        long discountAmount,
        long total,
        java.util.List<String> couponCodes,
        java.util.List<CartItemSnapshot> items
    ) {}

    record CartItemSnapshot(
        String productId,
        String variantId,
        String sku,
        String productName,
        String supplierId,
        int quantity,
        long unitPrice,
        long lineTotal
    ) {}
}
