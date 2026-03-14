package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.event.CartAbandonedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * Contains customized post Save Hook for the ACTIVE State ID.
 */
public class ACTIVECartPostSaveHook implements PostSaveHook<Cart> {

    @Autowired
    private CartEventPublisher cartEventPublisher;

    private static final List<String> CHECKOUT_STATES = Arrays.asList("CHECKOUT_IN_PROGRESS", "PAYMENT_PENDING",
            "PAYMENT_FAILED");

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        // If we transition to ACTIVE from a checkout-related state, it means checkout
        // was abandoned.
        if (startState != null && CHECKOUT_STATES.contains(startState.getStateId())) {
            CartAbandonedEvent event = new CartAbandonedEvent(cart.getId(), LocalDateTime.now());
            cartEventPublisher.publishCartAbandoned(event);
        }
    }
}
