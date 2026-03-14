package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.repository.CartEventPublisher;
import com.homebase.ecom.shared.event.CartAbandonedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;

/**
 * Contains customized post Save Hook for the EXPIRED State ID.
 */
public class EXPIREDCartPostSaveHook implements PostSaveHook<Cart> {

    @Autowired
    private CartEventPublisher cartEventPublisher;

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        CartAbandonedEvent event = new CartAbandonedEvent(cart.getId(), LocalDateTime.now());
        cartEventPublisher.publishCartAbandoned(event);
    }
}
