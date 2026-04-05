package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.event.CheckoutExpiredEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for EXPIRED state.
 * Publishes CheckoutExpiredEvent -- triggers cart restoration, inventory release.
 */
public class EXPIREDCheckoutPostSaveHook extends AbstractCheckoutEventPostSaveHook {

    public EXPIREDCheckoutPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CheckoutEvent buildEvent(Checkout checkout, TransientMap map) {
        return new CheckoutExpiredEvent(
                checkout.getId(),
                checkout.getCartId(),
                LocalDateTime.now()
        );
    }
}
