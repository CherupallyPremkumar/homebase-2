package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.MergeCartPayload;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.dto.OfferDto;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Action to merge guest cart items into a user cart.
 */
public class MergeCartAction extends AbstractCartAction<MergeCartPayload> {

    private static final Logger log = LoggerFactory.getLogger(MergeCartAction.class);

    @Override
    public void transitionTo(Cart cart,
            MergeCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.items == null || payload.items.isEmpty()) {
            return;
        }

        for (MergeCartPayload.MergeItemPayload itemPayload : payload.items) {
            try {
                OfferDto offer = validateAndGetOffer(itemPayload.productId, itemPayload.quantity);

                CartItem existingItem = cart.getItems().stream()
                        .filter(item -> item.getProductId().equals(itemPayload.productId))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + itemPayload.quantity);
                    // Update to latest price/seller
                    existingItem.setPrice(offer.getPrice());
                    existingItem.setSellerId(offer.getSellerId());
                    log.debug("Merged existing item: productId={}, newQuantity={}", itemPayload.productId,
                            existingItem.getQuantity());
                } else {
                    CartItem newItem = new CartItem();
                    newItem.setProductId(itemPayload.productId);
                    newItem.setQuantity(itemPayload.quantity);
                    newItem.setPrice(offer.getPrice());
                    newItem.setSellerId(offer.getSellerId());
                    cart.addItem(newItem);
                    log.debug("Added new item during merge: productId={}, quantity={}", itemPayload.productId,
                            itemPayload.quantity);
                }
            } catch (Exception e) {
                // Log and continue with other items if one fails validation
                // Alternatively, we could fail the whole merge, but continuing might be better
                // UX
                log.error("Failed to merge item: productId={}. Reason: {}", itemPayload.productId, e.getMessage());
            }
        }
        cart.getTransientMap().previousPayload = payload;
    }
}
