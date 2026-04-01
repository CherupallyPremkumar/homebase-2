package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.event.CheckoutPaymentFailedEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for PAYMENT_FAILED state.
 * Publishes CheckoutPaymentFailedEvent -- triggers notification to customer.
 */
public class PAYMENT_FAILEDCheckoutPostSaveHook extends AbstractCheckoutEventPostSaveHook {

    public PAYMENT_FAILEDCheckoutPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CheckoutEvent buildEvent(Checkout checkout, TransientMap map) {
        return new CheckoutPaymentFailedEvent(
                checkout.getId(),
                checkout.getCartId(),
                checkout.getFailureReason(),
                LocalDateTime.now()
        );
    }
}
