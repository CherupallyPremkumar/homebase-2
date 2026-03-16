package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.CompleteCheckoutCartPayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

/**
 * STM transition action for completeCheckout event (SYSTEM-triggered).
 * Called by Checkout service via Kafka after order is created.
 * Marks the cart as converted — no further operations allowed.
 */
public class CompleteCheckoutCartAction extends AbstractCartAction<CompleteCheckoutCartPayload> {

    @Override
    public void transitionTo(Cart cart, CompleteCheckoutCartPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.orderId == null || payload.orderId.isBlank()) {
            throw new IllegalArgumentException("orderId is required for completeCheckout");
        }

        // Store the order reference
        cart.description = "Order: " + payload.orderId;

        logActivity(cart, "completeCheckout",
                "Checkout completed, orderId: " + payload.orderId
                + ", total: " + cart.getTotal());
    }
}
