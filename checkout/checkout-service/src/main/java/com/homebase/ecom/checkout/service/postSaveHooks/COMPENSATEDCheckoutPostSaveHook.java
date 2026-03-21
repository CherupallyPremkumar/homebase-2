package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutCompensatedEvent;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for COMPENSATED state.
 * Publishes CheckoutCompensatedEvent -- triggers cart.cancelCheckout (restores cart).
 */
public class COMPENSATEDCheckoutPostSaveHook extends AbstractCheckoutEventPostSaveHook {

    public COMPENSATEDCheckoutPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CheckoutEvent buildEvent(Checkout checkout, TransientMap map) {
        return new CheckoutCompensatedEvent(
                checkout.getId(),
                checkout.getCartId(),
                checkout.getFailureReason(),
                LocalDateTime.now()
        );
    }
}
