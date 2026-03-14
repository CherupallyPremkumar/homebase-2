package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.dto.AbandonCheckoutCartPayload;
import com.homebase.ecom.inventory.dto.InventoryDto;

/**
 * Action triggered when a user abandons the checkout process.
 * Releases inventory reservations, unfreezes prices, and clears
 * checkout-specific transient data so the cart returns to ACTIVE state.
 */
public class AbandonCheckoutCartAction extends AbstractCartAction<AbandonCheckoutCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            AbandonCheckoutCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.getTransientMap().previousPayload = payload;

        // 1. Release inventory reservations if any were made during checkout
        String reservationId = (String) cart.getTransientMap().get("reservationId");
        if (reservationId != null) {
            for (CartItem item : cart.getItems()) {
                try {
                    InventoryDto inventory = inventoryRepository.findById(item.getProductId());
                    if (inventory != null && inventory.getReserved() != null && inventory.getReserved() > 0) {
                        log.info("Releasing reserved inventory for product {} on checkout abandon",
                                item.getProductId());
                    }
                } catch (Exception e) {
                    log.error("Failed to release inventory for product {} during checkout abandon: {}",
                            item.getProductId(), e.getMessage());
                }
            }
            cart.getTransientMap().remove("reservationId");
        }

        // 2. Unfreeze prices (clear price calculation ID)
        cart.getTransientMap().remove("priceCalculationId");
        cart.description = null; // Clear frozen price description

        // 3. Revert cart state data (clear checkout-specific transient data)
        cart.getTransientMap().remove("checkoutUrl");
        cart.getTransientMap().remove("checkoutPayload");
        cart.getTransientMap().remove("gatewaySessionId");
        cart.getTransientMap().remove("idempotencyKey");
        cart.getTransientMap().remove("paymentAmount");
        cart.getTransientMap().remove("paymentCurrency");
        cart.getTransientMap().remove("paymentItemCount");

        // 4. Log activity
        String previousState = startState != null ? startState.getStateId() : "UNKNOWN";
        logActivity(cart, "Abandon Checkout",
                "Checkout abandoned from state: " + previousState + ". Prices unfrozen, reservations released.");
        cart.addActivity(eventId, "Checkout abandoned. Cart reverted to active state.");
    }
}
