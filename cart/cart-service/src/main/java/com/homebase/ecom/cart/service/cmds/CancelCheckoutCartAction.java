package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

/**
 * STM transition action for cancelCheckout event.
 * Releases inventory reservation and returns cart to ACTIVE.
 */
public class CancelCheckoutCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // TODO: Call InventoryReservationPort.releaseItems() when wired

        logActivity(cart, "cancelCheckout", "Checkout cancelled, cart returned to ACTIVE");
    }
}
