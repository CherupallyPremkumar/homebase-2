package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.promo.service.PromotionService;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * OWIZ saga step 6: Commit coupon/promo usage.
 * Validates coupons via promo-client's PromotionService.
 * Full usage-tracking (marking coupons as used per order) will be added
 * when PromotionService exposes a commitUsage API.
 */
public class CommitPromoCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(CommitPromoCommand.class);

    @Autowired(required = false)
    private PromotionService promotionServiceClient;

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();
        if (promotionServiceClient != null && checkout.getCouponCodes() != null && !checkout.getCouponCodes().isEmpty()) {
            for (String coupon : checkout.getCouponCodes()) {
                boolean valid = promotionServiceClient.validateCoupon(coupon);
                if (!valid) {
                    throw new RuntimeException("Coupon '" + coupon + "' is no longer valid");
                }
                log.info("[CHECKOUT SAGA] Committed promo {} for checkout {}", coupon, checkout.getId());
            }
        }
        checkout.setLastCompletedStep("commitPromo");
    }
}
