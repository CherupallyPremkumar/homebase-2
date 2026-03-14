package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import com.homebase.ecom.cart.model.Cart;

/**
 * Action triggered on successful payment.
 */
public class PaymentSuccessCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.addActivity(eventId, "Payment successfully processed at the gateway.");

        // Any additional logic like clearing transient session data can go here
    }
}
