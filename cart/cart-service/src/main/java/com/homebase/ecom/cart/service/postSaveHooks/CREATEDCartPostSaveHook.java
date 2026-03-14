package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.event.CartCreatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Post-save hook for the CREATED state.
 * Publishes a CartCreatedEvent for analytics tracking (funnel analysis, session tracking).
 */
public class CREATEDCartPostSaveHook implements PostSaveHook<Cart> {

    @Autowired
    private CartEventPublisher cartEventPublisher;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        // Determine source from transient map (web/mobile/api), default to "api"
        String source = "api";
        if (map != null && map.get("source") != null) {
            source = (String) map.get("source");
        }

        CartCreatedEvent event = new CartCreatedEvent(
                cart.getId(),
                cart.getUserId(),
                source,
                LocalDateTime.now());

        cartEventPublisher.publishCartCreated(event);
    }
}
