package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.dto.UpdateQuantityCartPayload;

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
public class UpdateQuantityCartAction extends AbstractSTMTransitionAction<Cart,

        UpdateQuantityCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            UpdateQuantityCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(payload.productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(payload.quantity));

        cart.transientMap.previousPayload = payload;
    }

}
