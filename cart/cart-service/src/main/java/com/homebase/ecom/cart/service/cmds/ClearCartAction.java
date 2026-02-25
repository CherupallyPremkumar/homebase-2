package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.workflow.param.MinimalPayload;

public class ClearCartAction extends AbstractSTMTransitionAction<Cart, MinimalPayload> {
    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        cart.getItems().clear();
    }
}
