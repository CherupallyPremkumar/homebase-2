package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

/**
 * STM transition action for expire event (SYSTEM-triggered).
 * Called by scheduled job when cart has passed its expiresAt timestamp.
 * Transitions directly to EXPIRED — terminal state.
 */
public class ExpireCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        logActivity(cart, "expire",
                "Cart expired from state " + startState.getStateId()
                + ", items: " + cart.getItems().size());
    }
}
