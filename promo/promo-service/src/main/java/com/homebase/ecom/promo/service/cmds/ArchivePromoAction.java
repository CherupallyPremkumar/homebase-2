package com.homebase.ecom.promo.service.cmds;

import com.homebase.ecom.promo.dto.ArchivePromoPayload;
import com.homebase.ecom.promo.model.Coupon;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Archives a promo for historical record keeping.
 * Terminal state -- no further transitions possible.
 */
public class ArchivePromoAction extends AbstractPromoAction<ArchivePromoPayload> {
    @Override
    public void transitionTo(Coupon coupon, ArchivePromoPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        coupon.addActivity("archive", "Promo archived from " + startState.getStateId());
        super.transitionTo(coupon, payload, startState, eventId, endState, stm, transition);
    }
}
