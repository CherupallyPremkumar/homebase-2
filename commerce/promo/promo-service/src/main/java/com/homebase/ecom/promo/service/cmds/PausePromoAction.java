package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.PausePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Temporarily pauses a promo code. The promo will not be applicable to orders while paused.
 */
public class PausePromoAction extends AbstractPromoAction<PausePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, PausePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        coupon.addActivity("pause", "Promo temporarily paused");
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
