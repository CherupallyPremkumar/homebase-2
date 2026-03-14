package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import com.homebase.ecom.cart.model.Cart;

/**
 * Action triggered when a payment webhook is missed and reconciliation is
 * needed.
 */
public class ReconciliationPendingCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String orderId = (String) cart.getTransientMap().get("orderId");
        if (orderId != null) {
            log.info("Order {} reconciliation pending. Inventory release should be handled by order module if needed.",
                    orderId);
        }

        cart.addActivity(eventId, "Payment reconciliation pending. Webhook might have been missed.");
    }
}
