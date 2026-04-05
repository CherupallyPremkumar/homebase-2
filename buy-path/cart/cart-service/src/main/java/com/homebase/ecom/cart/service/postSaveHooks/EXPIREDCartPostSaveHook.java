package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.event.CartExpiredEvent;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for EXPIRED state.
 * Publishes cart expired event -- triggers inventory release.
 */
public class EXPIREDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public EXPIREDCartPostSaveHook(CartEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CartEvent buildEvent(Cart cart, TransientMap map) {
        return new CartExpiredEvent(cart.getId(), cart.getCustomerId(), LocalDateTime.now());
    }
}
