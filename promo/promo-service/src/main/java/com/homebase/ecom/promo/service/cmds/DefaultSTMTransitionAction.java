package com.homebase.ecom.promo.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.promo.model.Coupon;

/**
 * This class is invoked if no specific transition action is specified
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
                extends AbstractSTMTransitionAction<Coupon, PayloadType> {
        @Override
        public void transitionTo(Coupon coupon, PayloadType payload,
                        State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                        Transition transition) {
        }
}
