package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.UpdateQuantityCartPayload;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.dto.OfferDto;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * Contains customized logic for the transition. Common logic resides at
 * {@link DefaultSTMTransitionAction}
 */
public class UpdateQuantityCartAction extends AbstractCartAction<UpdateQuantityCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            UpdateQuantityCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Consistent validation for update quantity
        OfferDto offer = validateAndGetOffer(payload.productId, payload.quantity);

        cart.getItems().stream()
                .filter(item -> item.getProductId().equals(payload.productId))
                .findFirst()
                .ifPresent(item -> {
                    CartItem checkItem = new CartItem();
                    checkItem.setProductId(payload.productId);
                    checkItem.setQuantity(payload.quantity);

                    cartPolicyValidator.validate(cart, checkItem, offer);

                    item.setQuantity(payload.quantity);
                    item.setPrice(offer.getPrice());
                    item.setSellerId(offer.getSellerId());
                });

        cart.getTransientMap().previousPayload = payload;
    }
}
