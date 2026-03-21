package com.homebase.ecom.cart.service.postSaveHooks;

import com.homebase.ecom.cart.event.CartEvent;
import com.homebase.ecom.cart.event.CartMergedEvent;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.port.CartEventPublisherPort;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for MERGED state.
 * Publishes cart merged event -- triggers inventory release
 * for the source (guest) cart's reservations.
 */
public class MERGEDCartPostSaveHook extends AbstractCartEventPostSaveHook {

    public MERGEDCartPostSaveHook(CartEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CartEvent buildEvent(Cart cart, TransientMap map) {
        String targetCartId = map != null ? (String) map.get("targetCartId") : null;
        return new CartMergedEvent(cart.getId(), targetCartId, cart.getCustomerId(), LocalDateTime.now());
    }
}
