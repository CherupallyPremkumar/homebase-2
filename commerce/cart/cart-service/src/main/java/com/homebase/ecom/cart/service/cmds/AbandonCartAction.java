package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.time.LocalDateTime;

/**
 * STM transition action for abandon event (SYSTEM-triggered).
 * Called by scheduled job when cart has been idle past abandonmentThresholdHours.
 * Cart can be reactivated later via the reactivate event.
 */
public class AbandonCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        logActivity(cart, "abandon",
                "Cart abandoned from state " + startState.getStateId()
                + ", items: " + cart.getItems().size()
                + ", total: " + cart.getTotal());
    }
}
