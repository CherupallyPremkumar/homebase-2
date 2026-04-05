package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelCheckoutAction extends AbstractSTMTransitionAction<Checkout, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CancelCheckoutAction.class);

    @Override
    public void transitionTo(Checkout checkout, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = payload.getComment() != null ? payload.getComment() : "user/system";
        checkout.setFailureReason("Cancelled by " + reason);

        log.info("[CHECKOUT] Cancelled checkoutId={}, from state={}",
                checkout.getId(), startState.getStateId());
    }
}
