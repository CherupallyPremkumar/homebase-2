package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.event.CartAbandonedEvent;
import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for ABANDONED state.
 * Publishes cart abandoned event -- triggers inventory release.
 */
public class ABANDONEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public ABANDONEDCartPostSaveHook(CartEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CartEvent buildEvent(Cart cart, TransientMap map) {
        return new CartAbandonedEvent(cart.getId(), LocalDateTime.now());
    }
}
