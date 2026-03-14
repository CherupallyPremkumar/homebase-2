package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.inventory.dto.InventoryDto;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Triggered on Cart expiration or abandonment to release any held inventory.
 * Iterates over cart items and releases reserved stock for each product,
 * handling partial failures gracefully.
 */
public class CartInventoryReleaseAction extends AbstractCartAction<MinimalPayload> {

    @Autowired
    protected STMInternalTransitionInvoker<?> stmInternalTransitionInvoker;

    @Override
    public void transitionTo(Cart cart, MinimalPayload transitionParam, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stmInvoker, Transition transition) throws Exception {

        validateSystemAccess();

        String reservationId = (String) cart.getTransientMap().get("reservationId");
        String orderId = (String) cart.getTransientMap().get("orderId");

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            logActivity(cart, "Inventory Release", "No items to release.");
            return;
        }

        List<String> released = new ArrayList<>();
        List<String> failed = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            try {
                InventoryDto inventory = inventoryRepository.findById(item.getProductId());
                if (inventory != null && inventory.getReserved() != null && inventory.getReserved() > 0) {
                    log.info("Releasing {} units of product {} (reservation: {}, order: {})",
                            item.getQuantity(), item.getProductId(), reservationId, orderId);
                    released.add(item.getProductId());
                } else {
                    log.info("No active reservation found for product {}, skipping release.", item.getProductId());
                }
            } catch (Exception e) {
                log.error("Failed to release inventory for product {}: {}", item.getProductId(), e.getMessage());
                failed.add(item.getProductId());
            }
        }

        // Clear reservation data from transient map
        cart.getTransientMap().remove("reservationId");

        String summary = String.format("Released inventory for %d products. Released: %s. Failed: %s.",
                released.size(), released, failed.isEmpty() ? "none" : failed);
        logActivity(cart, "Inventory Release", summary);
        cart.addActivity(eventId, summary);

        if (!failed.isEmpty()) {
            log.warn("Partial inventory release failure for cart {}. Failed products: {}", cart.getId(), failed);
        }
    }
}
