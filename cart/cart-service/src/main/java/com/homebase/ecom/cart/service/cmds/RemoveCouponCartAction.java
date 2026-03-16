package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.RemoveCouponCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * STM transition action for removeCoupon event.
 * Removes coupon code and recalculates pricing without it.
 */
public class RemoveCouponCartAction extends AbstractCartAction<RemoveCouponCartPayload> {

    @Override
    public void transitionTo(Cart cart, RemoveCouponCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (!cart.getCouponCodes().contains(payload.couponCode)) {
            throw new IllegalArgumentException("Coupon not found: " + payload.couponCode);
        }

        cart.removeCouponCode(payload.couponCode);

        // Recalculate pricing without the removed coupon
        recalculatePricing(cart);

        logActivity(cart, "removeCoupon", "Removed coupon " + payload.couponCode
                + ", discount: " + cart.getDiscountAmount()
                + ", total: " + cart.getTotal());
    }
}
