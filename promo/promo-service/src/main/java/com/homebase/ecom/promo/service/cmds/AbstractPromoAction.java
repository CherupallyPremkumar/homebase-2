package com.homebase.ecom.promo.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.promo.model.Coupon;

public abstract class AbstractPromoAction<P extends MinimalPayload> extends AbstractSTMTransitionAction<Coupon, P> {
    @Override
    public void transitionTo(Coupon coupon, P payload, State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        coupon.getTransientMap().put("previousPayload", payload);
    }
}
