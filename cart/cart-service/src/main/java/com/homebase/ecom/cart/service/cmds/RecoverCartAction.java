package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.RecoverCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;

public class RecoverCartAction extends AbstractCartAction<RecoverCartPayload> {

    @Override
    public void transitionTo(Cart cart, RecoverCartPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, org.chenile.stm.model.Transition transition)
            throws Exception {
        validateCustomerAccess();
        logActivity(cart, "Recover Cart", "Cart recovered by customer.");
    }
}
