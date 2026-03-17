package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartAbandonedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for ABANDONED state.
 * Publishes CartAbandonedEvent after transaction commits — triggers inventory release.
 */
public class ABANDONEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public ABANDONEDCartPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        super(chenilePub, objectMapper);
    }

    @Override
    protected Object buildEvent(State startState, State endState, Cart cart, TransientMap map) {
        return new CartAbandonedEvent(cart.getId(), LocalDateTime.now());
    }

    @Override
    protected String eventType() {
        return CartAbandonedEvent.EVENT_TYPE;
    }
}
