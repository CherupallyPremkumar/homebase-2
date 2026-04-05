package com.homebase.ecom.checkout.domain.port;

import java.util.List;

/**
 * Port for committing/releasing coupon usage during checkout.
 * Adapter calls Promo service.
 */
public interface PromoCommitPort {

    /**
     * Commits coupon usage — marks coupons as used for this checkout.
     * @throws RuntimeException if any coupon is no longer valid
     */
    void commitCoupons(String checkoutId, String customerId, List<String> couponCodes);

    /**
     * Releases committed coupons — used for compensation.
     */
    void releaseCoupons(String checkoutId, List<String> couponCodes);
}
