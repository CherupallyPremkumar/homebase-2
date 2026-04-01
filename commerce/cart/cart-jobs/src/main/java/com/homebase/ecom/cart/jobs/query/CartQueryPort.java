package com.homebase.ecom.cart.jobs.query;

import java.util.List;

/**
 * Port for querying cart-query read model.
 * Implemented by adapter that calls cart-query service (Chenile proxy or direct).
 * All queries return cart IDs only — cart-jobs never loads full cart objects.
 */
public interface CartQueryPort {

    /**
     * Find ACTIVE carts containing a specific variant.
     */
    List<String> findActiveCartsWithVariant(String variantId);

    /**
     * Find ACTIVE carts that have a specific coupon applied.
     */
    List<String> findActiveCartsWithCoupon(String couponCode);

    /**
     * Find carts past their expiresAt timestamp.
     * Includes ACTIVE, ABANDONED, and CHECKOUT_INITIATED states.
     */
    List<String> findExpiredCarts();

    /**
     * Find ACTIVE carts idle longer than the given threshold.
     */
    List<String> findIdleCarts(int thresholdHours);
}
