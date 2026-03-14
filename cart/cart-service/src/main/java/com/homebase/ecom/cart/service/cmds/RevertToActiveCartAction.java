package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import com.homebase.ecom.cart.model.Cart;

/**
 * Action to revert the cart state to ACTIVE.
 */
public class RevertToActiveCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.addActivity(eventId, "Reverting cart to active state.");
        // Clear any checkout specific data if necessary (like checkout session ID)
        cart.getTransientMap().remove("checkoutUrl");
    }
}
