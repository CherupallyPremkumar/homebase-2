package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.AddItemCartPayload;
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
public class AddItemCartAction extends AbstractCartAction<AddItemCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            AddItemCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        OfferDto offer = validateAndGetOffer(payload.productId, payload.quantity);

        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(payload.productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            // Validate the hypothetical state of the item
            CartItem checkItem = new CartItem();
            checkItem.setProductId(payload.productId);
            checkItem.setQuantity(existingItem.getQuantity() + payload.quantity);

            cartPolicyValidator.validate(cart, checkItem, offer);

            existingItem.setQuantity(existingItem.getQuantity() + payload.quantity);
            // Update to latest price/seller if needed
            existingItem.setPrice(offer.getPrice());
            existingItem.setSellerId(offer.getSellerId());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(payload.productId);
            newItem.setQuantity(payload.quantity);
            newItem.setPrice(offer.getPrice());
            newItem.setSellerId(offer.getSellerId());

            cartPolicyValidator.validate(cart, newItem, offer);

            cart.addItem(newItem);
        }
        cart.getTransientMap().previousPayload = payload;
    }
}
