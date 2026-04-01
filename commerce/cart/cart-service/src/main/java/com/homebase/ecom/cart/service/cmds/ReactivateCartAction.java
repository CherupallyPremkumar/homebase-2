package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * STM transition action for reactivate event.
 * Reactivates an abandoned cart — resets expiration timer and
 * removes items that are no longer available (Amazon pattern).
 */
public class ReactivateCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Re-validate inventory — remove items no longer available
        List<String> removedItems = new ArrayList<>();
        List<CartItem> validItems = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            if (inventoryCheckPort.isAvailable(item.getVariantId(), item.getQuantity())) {
                validItems.add(item);
            } else {
                removedItems.add(item.getProductName() + " (" + item.getVariantId() + ")");
            }
        }

        if (!removedItems.isEmpty()) {
            cart.setItems(validItems);
            log.info("Removed unavailable items from reactivated cart {}: {}", cart.getId(), removedItems);
        }

        // Recalculate pricing with remaining items
        if (!cart.getItems().isEmpty()) {
            recalculatePricing(cart);
        }

        // Reset expiration
        int expirationHours = cartPolicyValidator.getCartExpirationHours();
        cart.setExpiresAt(LocalDateTime.now().plusHours(expirationHours));

        logActivity(cart, "reactivate",
                "Cart reactivated from " + startState.getStateId()
                + (removedItems.isEmpty() ? "" : ", removed unavailable: " + removedItems));
    }
}
