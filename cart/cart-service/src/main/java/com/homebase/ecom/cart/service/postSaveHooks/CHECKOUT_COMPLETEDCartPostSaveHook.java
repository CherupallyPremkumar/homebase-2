package com.homebase.ecom.cart.service.postSaveHooks;

import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.shared.event.CartCheckoutCompletedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for CHECKOUT_COMPLETED state.
 * Publishes CartCheckoutCompletedEvent after transaction commits.
 */
public class CHECKOUT_COMPLETEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public CHECKOUT_COMPLETEDCartPostSaveHook(ChenilePub chenilePub, ObjectMapper objectMapper) {
        super(chenilePub, objectMapper);
    }

    @Override
    protected Object buildEvent(State startState, State endState, Cart cart, TransientMap map) {
        CartCheckoutCompletedEvent event = new CartCheckoutCompletedEvent();
        event.setCartId(cart.getId());
        event.setUserId(cart.getCustomerId());
        event.setTotalAmount(cart.getSubtotal().getAmount());
        event.setCurrency(cart.getCurrency());
        event.setTimestamp(LocalDateTime.now());
        return event;
    }

    @Override
    protected String eventType() {
        return CartCheckoutCompletedEvent.EVENT_TYPE;
    }
}
