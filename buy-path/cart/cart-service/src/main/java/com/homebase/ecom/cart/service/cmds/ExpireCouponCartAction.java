package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.ExpireCouponCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Removes an expired coupon from the cart and recalculates pricing.
 *
 * Triggered by cart-jobs CouponExpiredTranslator when Promo service
 * publishes a COUPON_EXPIRED event.
 */
public class ExpireCouponCartAction extends AbstractCartAction<ExpireCouponCartPayload> {

    @Override
    public void transitionTo(Cart cart, ExpireCouponCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.couponCode == null) {
            log.warn("ExpireCoupon event missing couponCode for cartId={}", cart.getId());
            return;
        }

        if (cart.getCouponCodes().contains(payload.couponCode)) {
            cart.removeCouponCode(payload.couponCode);
            recalculatePricing(cart);
            logActivity(cart, "expireCoupon", "Coupon " + payload.couponCode + " expired and removed");
        } else {
            log.debug("Coupon {} not found in cartId={}, skipping", payload.couponCode, cart.getId());
        }
    }
}
