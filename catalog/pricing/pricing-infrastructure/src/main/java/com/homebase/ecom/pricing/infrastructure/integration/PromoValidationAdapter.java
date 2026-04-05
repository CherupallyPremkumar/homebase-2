package com.homebase.ecom.pricing.infrastructure.integration;

import com.homebase.ecom.pricing.domain.port.PromoValidationPort;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Infrastructure adapter: validates coupon codes against the Promo module.
 * Currently returns invalid — promo service not yet connected.
 * Will be replaced with a real adapter calling promo-client when Promo module
 * exposes coupon validation via its API.
 */
public class PromoValidationAdapter implements PromoValidationPort {

    private static final Logger log = LoggerFactory.getLogger(PromoValidationAdapter.class);

    @Override
    public CouponResult validate(String couponCode, Money cartTotal, String userId) {
        log.debug("Coupon validation for code={}, cartTotal={}, userId={} — promo service not connected",
                couponCode, cartTotal, userId);
        return CouponResult.invalid(couponCode, "Promo service not available");
    }
}
