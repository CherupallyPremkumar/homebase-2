package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutCompletedEvent;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;

/**
 * Post-save hook for COMPLETED state.
 * Publishes CheckoutCompletedEvent -- triggers cart.completeCheckout + order.create.
 */
public class COMPLETEDCheckoutPostSaveHook extends AbstractCheckoutEventPostSaveHook {

    public COMPLETEDCheckoutPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        super(eventPublisher);
    }

    @Override
    protected CheckoutEvent buildEvent(Checkout checkout, TransientMap map) {
        return new CheckoutCompletedEvent(
                checkout.getId(),
                checkout.getCartId(),
                checkout.getOrderId(),
                checkout.getCustomerId(),
                checkout.getPaymentId(),
                checkout.getTotal().getAmount(),
                checkout.getTotal().getCurrency(),
                LocalDateTime.now()
        );
    }
}
