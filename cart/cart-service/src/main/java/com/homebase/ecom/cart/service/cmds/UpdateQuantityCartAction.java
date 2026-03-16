package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.UpdateQuantityCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * STM transition action for updateQuantity event.
 * Updates quantity by variantId, re-checks inventory, then recalculates pricing.
 */
public class UpdateQuantityCartAction extends AbstractCartAction<UpdateQuantityCartPayload> {

    @Override
    public void transitionTo(Cart cart, UpdateQuantityCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        cartPolicyValidator.validateQuantity(payload.quantity);

        boolean found = cart.getItems().stream()
                .anyMatch(i -> i.getVariantId().equals(payload.variantId));
        if (!found) {
            throw new IllegalArgumentException("Variant not found in cart: " + payload.variantId);
        }

        // Re-check inventory for the new quantity
        if (!inventoryCheckPort.isAvailable(payload.variantId, payload.quantity)) {
            throw new IllegalStateException("Insufficient inventory for variant: " + payload.variantId);
        }

        cart.updateItemQuantity(payload.variantId, payload.quantity);

        // Recalculate pricing (volume discounts may change) and validate max cart value
        recalculatePricingAndValidateValue(cart);

        logActivity(cart, "updateQuantity", "Updated variant " + payload.variantId + " to qty " + payload.quantity);
    }
}
