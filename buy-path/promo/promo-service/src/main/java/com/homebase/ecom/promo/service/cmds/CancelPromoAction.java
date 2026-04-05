package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.CancelPromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Cancels a promo permanently. Only ADMIN can cancel.
 */
public class CancelPromoAction extends AbstractPromoAction<CancelPromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, CancelPromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = (payload.getReason() != null) ? payload.getReason() : "Promo cancelled by admin";
        coupon.addActivity("cancel", reason);
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
