package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.RemoveItemCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 */
public class RemoveItemCartAction extends AbstractCartAction<RemoveItemCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            RemoveItemCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.getItems().removeIf(item -> item.getProductId().equals(payload.productId));
        cart.getTransientMap().previousPayload = payload;
    }
}
