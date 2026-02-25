package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.dto.RemoveItemCartPayload;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 * <p>
 * Use this class if you want to augment the common logic for this specific
 * transition
 * </p>
 * <p>
 * Use a customized payload if required instead of MinimalPayload
 * </p>
 */
public class RemoveItemCartAction extends AbstractSTMTransitionAction<Cart,

        RemoveItemCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            RemoveItemCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.getItems().removeIf(item -> item.getProductId().equals(payload.productId));
        cart.transientMap.previousPayload = payload;
    }

}
