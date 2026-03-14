package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ActivatePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Activates a promo code from DRAFT state.
 * Validates that the expiry date is in the future before activation.
 */
public class ActivatePromoAction extends AbstractPromoAction<ActivatePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ActivatePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Validate expiry date is in the future
        if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot activate a promo code with an expiry date in the past");
        }
        coupon.addActivity("activate", "Promo code activated");
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
