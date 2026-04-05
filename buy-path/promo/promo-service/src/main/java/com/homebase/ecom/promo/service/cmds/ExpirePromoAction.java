package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ExpirePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Expires a promo code. Triggered automatically when end date passes
 * or usage limit is reached, or manually by system/admin.
 */
public class ExpirePromoAction extends AbstractPromoAction<ExpirePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ExpirePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        String reason = (payload.getReason() != null) ? payload.getReason() : "Promo expired";
        coupon.addActivity("expire", reason);
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
