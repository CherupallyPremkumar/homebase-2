package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.workflow.param.MinimalPayload;

public class ReserveTimeoutAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, org.chenile.stm.model.Transition transition)
            throws Exception {
        validateSystemAccess();
        logActivity(cart, "Reserve Timeout", "Inventory reservation timed out.");
    }
}
