package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ResumePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Resumes a paused promo code back to ACTIVE state.
 */
public class ResumePromoAction extends AbstractPromoAction<ResumePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ResumePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Validate end date is still in the future when resuming
        if (coupon.getEndDate() != null && coupon.getEndDate().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot resume a promo that has passed its end date");
        }
        coupon.addActivity("resume", "Promo resumed from paused state");
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
