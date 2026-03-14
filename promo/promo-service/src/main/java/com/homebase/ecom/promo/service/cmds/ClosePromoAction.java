package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ClosePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Admin closes a promo code permanently.
 */
public class ClosePromoAction extends AbstractPromoAction<ClosePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ClosePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = (payload.getReason() != null) ? payload.getReason() : "Promo closed by admin";
        coupon.addActivity("close", reason);
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
