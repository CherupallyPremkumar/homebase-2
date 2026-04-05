package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.RemoveItemCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * STM transition action for removeItem event.
 * Removes item by variantId, then recalculates pricing.
 */
public class RemoveItemCartAction extends AbstractCartAction<RemoveItemCartPayload> {

    @Override
    public void transitionTo(Cart cart, RemoveItemCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        boolean existed = cart.getItems().stream()
                .anyMatch(i -> i.getVariantId().equals(payload.variantId));
        if (!existed) {
            throw new IllegalArgumentException("Variant not found in cart: " + payload.variantId);
        }

        cart.removeItem(payload.variantId);

        // Recalculate pricing after removal (pricing handles empty cart too)
        recalculatePricing(cart);

        logActivity(cart, "removeItem", "Removed variant " + payload.variantId);
    }
}
