package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartMergedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for MERGED state.
 * Publishes CartMergedEvent after transaction commits — triggers inventory release
 * for the source (guest) cart's reservations.
 */
public class MERGEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public MERGEDCartPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        super(chenilePub, objectMapper);
    }

    @Override
    protected Object buildEvent(State startState, State endState, Cart cart, TransientMap map) {
        String targetCartId = map != null ? (String) map.get("targetCartId") : null;
        return new CartMergedEvent(
                cart.getId(),
                targetCartId,
                cart.getCustomerId(),
                LocalDateTime.now());
    }

    @Override
    protected String eventType() {
        return CartMergedEvent.EVENT_TYPE;
    }
}
