package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ReactivatePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Reactivates an expired promo code with potentially new dates.
 */
public class ReactivatePromoAction extends AbstractPromoAction<ReactivatePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ReactivatePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = (payload.getReason() != null) ? payload.getReason() : "Promo reactivated";
        coupon.addActivity("reactivate", reason);
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
