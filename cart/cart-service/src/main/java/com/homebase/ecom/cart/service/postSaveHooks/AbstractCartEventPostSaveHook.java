package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for Cart post-save hooks that publish domain events.
 * Each subclass builds the appropriate CartEvent and delegates to the single-method port.
 */
public abstract class AbstractCartEventPostSaveHook implements PostSaveHook<Cart> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final CartEventPublisherPort eventPublisher;

    protected AbstractCartEventPostSaveHook(CartEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected abstract CartEvent buildEvent(Cart cart, TransientMap map);

    @Override
    public void execute(State startState, State endState, Cart cart, TransientMap map) {
        CartEvent event = buildEvent(cart, map);
        eventPublisher.publish(event);
    }
}
