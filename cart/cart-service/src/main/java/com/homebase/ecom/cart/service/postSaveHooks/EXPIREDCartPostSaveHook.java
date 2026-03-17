package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartExpiredEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for EXPIRED state.
 * Publishes CartExpiredEvent after transaction commits — triggers inventory release.
 */
public class EXPIREDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public EXPIREDCartPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        super(chenilePub, objectMapper);
    }

    @Override
    protected Object buildEvent(State startState, State endState, Cart cart, TransientMap map) {
        return new CartExpiredEvent(cart.getId(), cart.getCustomerId(), LocalDateTime.now());
    }

    @Override
    protected String eventType() {
        return CartExpiredEvent.EVENT_TYPE;
    }
}
