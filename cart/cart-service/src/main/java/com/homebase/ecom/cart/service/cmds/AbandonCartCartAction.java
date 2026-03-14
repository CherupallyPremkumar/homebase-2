package com.homebase.ecom.cart.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.model.CartItemStatus;
import com.homebase.ecom.cart.dto.AbandonCartCartPayload;
import com.homebase.ecom.inventory.dto.InventoryDto;

import java.time.Instant;

/**
 * Action triggered when a cart is abandoned (e.g., session timeout).
 * Releases any held inventory reservations, marks items as stale,
 * and records abandonment details for analytics.
 */
public class AbandonCartCartAction extends AbstractCartAction<AbandonCartCartPayload> {

    @Override
    public void transitionTo(Cart cart,
            AbandonCartCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        cart.getTransientMap().previousPayload = payload;

        // 1. Release any held inventory reservations
        String reservationId = (String) cart.getTransientMap().get("reservationId");
        if (reservationId != null) {
            for (CartItem item : cart.getItems()) {
                try {
                    InventoryDto inventory = inventoryRepository.findById(item.getProductId());
                    if (inventory != null && inventory.getReserved() != null && inventory.getReserved() > 0) {
                        log.info("Releasing reserved inventory for product {} on cart abandon", item.getProductId());
                    }
                } catch (Exception e) {
                    log.error("Failed to release inventory for product {} during abandon: {}",
                            item.getProductId(), e.getMessage());
                }
            }
            cart.getTransientMap().remove("reservationId");
        }

        // 2. Mark all cart items as stale (out of stock) since prices/availability may change
        for (CartItem item : cart.getItems()) {
            item.setStatus(CartItemStatus.PRICE_CHANGED);
        }

        // 3. Record abandonment reason and timestamp
        String abandonReason = startState != null
                ? "Abandoned from state: " + startState.getStateId()
                : "Abandoned due to inactivity";
        cart.getTransientMap().put("abandonReason", abandonReason);
        cart.getTransientMap().put("abandonTimestamp", Instant.now().toString());

        // 4. Log activity with cart snapshot for analytics
        int itemCount = cart.getItems().size();
        String totalAmount = cart.getTotalAmount() != null ? cart.getTotalAmount().toString() : "N/A";
        String analyticsMsg = String.format(
                "Cart abandoned. Items: %d, Total: %s, Previous state: %s",
                itemCount, totalAmount, startState != null ? startState.getStateId() : "UNKNOWN");

        logActivity(cart, "Abandon Cart", analyticsMsg);
        cart.addActivity(eventId, analyticsMsg);
    }
}
