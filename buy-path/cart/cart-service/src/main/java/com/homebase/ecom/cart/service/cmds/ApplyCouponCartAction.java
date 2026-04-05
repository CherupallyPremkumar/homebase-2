package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.ApplyCouponCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * STM transition action for applyCoupon event.
 * Adds coupon code, calls PricingPort to validate and calculate new discount/total.
 * PricingPort internally validates the coupon via Promo service.
 */
public class ApplyCouponCartAction extends AbstractCartAction<ApplyCouponCartPayload> {

    @Override
    public void transitionTo(Cart cart, ApplyCouponCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.couponCode == null || payload.couponCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Coupon code is required");
        }

        String coupon = payload.couponCode.trim().toUpperCase();

        if (cart.getCouponCodes().contains(coupon)) {
            throw new IllegalArgumentException("Coupon already applied: " + coupon);
        }

        cartPolicyValidator.validateCouponCount(cart);

        // Add coupon first, then recalculate
        cart.addCouponCode(coupon);

        try {
            // PricingPort validates coupon via Promo service and sets discount/total
            recalculatePricing(cart);
        } catch (IllegalStateException e) {
            // Pricing rejected the coupon — rollback
            cart.removeCouponCode(coupon);
            throw new IllegalArgumentException("Invalid coupon: " + e.getMessage());
        }

        logActivity(cart, "applyCoupon", "Applied coupon " + coupon
                + ", discount: " + cart.getDiscountAmount()
                + ", total: " + cart.getTotal());
    }
}
