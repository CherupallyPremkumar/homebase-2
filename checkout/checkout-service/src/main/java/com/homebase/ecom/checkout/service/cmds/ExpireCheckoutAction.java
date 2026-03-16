package com.homebase.ecom.checkout.service.cmds;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for 'expire' event.
 * Transitions to EXPIRED state when checkout times out.
 */
public class ExpireCheckoutAction extends AbstractSTMTransitionAction<Checkout, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ExpireCheckoutAction.class);

    @Override
    public void transitionTo(Checkout checkout, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        checkout.setFailureReason("Checkout expired");

        log.info("[CHECKOUT] Expired checkoutId={}", checkout.getId());
    }
}
