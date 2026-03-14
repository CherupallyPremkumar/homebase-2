package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import com.homebase.ecom.cart.model.Cart;

/**
 * Action triggered on failed payment.
 */
public class PaymentFailedCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String orderId = (String) cart.getTransientMap().get("orderId");
        if (orderId != null) {
            log.info("Order {} payment failed. Inventory release should be handled by order module.", orderId);
        }

        cart.addActivity(eventId, "Payment failed at the gateway.");
    }
}
