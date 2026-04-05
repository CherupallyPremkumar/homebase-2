package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutCancelledEvent;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for CANCELLED state.
 * Publishes CheckoutCancelledEvent -- triggers cart.cancelCheckout (restores cart).
 */
public class CANCELLEDCheckoutPostSaveHook extends AbstractCheckoutEventPostSaveHook {

    public CANCELLEDCheckoutPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CheckoutEvent buildEvent(Checkout checkout, TransientMap map) {
        return new CheckoutCancelledEvent(
                checkout.getId(),
                checkout.getCartId(),
                checkout.getFailureReason(),
                LocalDateTime.now()
        );
    }
}
