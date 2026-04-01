package com.homebase.ecom.checkout.infrastructure.integration;

import com.homebase.ecom.checkout.domain.port.PromoCommitPort;
import com.homebase.ecom.promo.dto.PromoApplicationResult;
import com.homebase.ecom.promo.dto.PromoCartDTO;
import com.homebase.ecom.promo.service.PromotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Driven adapter: commits/releases coupon usage via Promo service.
 * Delegates to promo-client's PromotionService proxy.
 *
 * Commit: validates + applies each coupon via applyPromo() to lock usage.
 * Release: currently the PromotionService API does not expose a release/rollback method,
 * so release logs a warning. When the promo-client adds a releaseCoupon endpoint,
 * this adapter is ready to delegate.
 */
public class PromoCommitAdapter implements PromoCommitPort {

    private static final Logger log = LoggerFactory.getLogger(PromoCommitAdapter.class);

    private final PromotionService promotionService;

    public PromoCommitAdapter(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @Override
    public void commitCoupons(String checkoutId, String customerId, List<String> couponCodes) {
        log.info("Committing coupons for checkout={}, customer={}, coupons={}",
                checkoutId, customerId, couponCodes);
        // TODO: delegate to promo-client commitCoupon endpoint
        if (couponCodes == null || couponCodes.isEmpty()) {
            log.debug("No coupons to commit for checkout={}", checkoutId);
            return;
        }

        PromoCartDTO cart = new PromoCartDTO();
        cart.setCartId(checkoutId);

        for (String code : couponCodes) {
            if (!promotionService.validateCoupon(code)) {
                log.error("Coupon {} is no longer valid during checkout={}", code, checkoutId);
                throw new RuntimeException("Coupon " + code + " is no longer valid");
            }

            PromoApplicationResult result = promotionService.applyPromo(cart, code);
            if (!result.isValid()) {
                log.error("Coupon {} application failed for checkout={}: {}",
                        code, checkoutId, result.getReasonIfInvalid());
                throw new RuntimeException("Coupon " + code + " application failed: " + result.getReasonIfInvalid());
            }
            log.debug("Coupon {} committed for checkout={}, discount={}", code, checkoutId, result.getDiscountAmount());
        }
        log.info("All coupons committed successfully for checkout={}", checkoutId);
    }

    @Override
    public void releaseCoupons(String checkoutId, List<String> couponCodes) {
        log.info("Releasing coupons for checkout={}, coupons={} (compensation)",
                checkoutId, couponCodes);
        // TODO: delegate to promo-client releaseCoupon endpoint
        // NOTE: PromotionService API does not yet expose a release/rollback method.
        // When promo-client adds releaseCoupon(checkoutId, code), delegate here.
        log.warn("Coupon release not yet supported by promo-client API. " +
                "Coupons {} for checkout={} may need manual reconciliation.", couponCodes, checkoutId);
    }
}
