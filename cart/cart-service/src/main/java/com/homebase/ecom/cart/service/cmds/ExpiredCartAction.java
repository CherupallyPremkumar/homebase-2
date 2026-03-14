package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.inventory.dto.InventoryDto;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;

import com.homebase.ecom.cart.model.Cart;

import java.util.ArrayList;
import java.util.List;

/**
 * Action triggered when a cart expires due to lock timeout, reserve timeout, or TTL.
 * Releases any active inventory reservations and records the expiration.
 */
public class ExpiredCartAction extends AbstractCartAction<MinimalPayload> {

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String previousState = startState != null ? startState.getStateId() : "UNKNOWN";

        // Release inventory if reservation was active (cart was in RESERVED or PAYMENT_PENDING state)
        String reservationId = (String) cart.getTransientMap().get("reservationId");
        String orderId = (String) cart.getTransientMap().get("orderId");

        if (reservationId != null || orderId != null) {
            log.info("Releasing inventory for cart {} (reservation: {}, order: {}) due to expiration from state {}",
                    cart.getId(), reservationId, orderId, previousState);

            List<String> released = new ArrayList<>();
            for (CartItem item : cart.getItems()) {
                try {
                    InventoryDto inventory = inventoryRepository.findById(item.getProductId());
                    if (inventory != null && inventory.getReserved() != null && inventory.getReserved() > 0) {
                        log.info("Releasing {} units of product {} due to cart expiration",
                                item.getQuantity(), item.getProductId());
                        released.add(item.getProductId());
                    }
                } catch (Exception e) {
                    log.error("Failed to release inventory for product {} during cart expiration: {}",
                            item.getProductId(), e.getMessage());
                }
            }

            // Clean up reservation data
            cart.getTransientMap().remove("reservationId");

            if (!released.isEmpty()) {
                logActivity(cart, "Inventory Release on Expiration",
                        "Released inventory for products: " + released);
            }
        }

        // Clear checkout-specific transient data
        cart.getTransientMap().remove("priceCalculationId");
        cart.getTransientMap().remove("checkoutUrl");
        cart.getTransientMap().remove("checkoutPayload");
        cart.getTransientMap().remove("gatewaySessionId");

        cart.addActivity(eventId,
                String.format("Cart expired from state %s. Reason: inactivity/timeout.", previousState));
    }
}
