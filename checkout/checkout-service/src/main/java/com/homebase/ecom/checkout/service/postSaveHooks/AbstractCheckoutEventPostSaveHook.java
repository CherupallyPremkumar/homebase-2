package com.homebase.ecom.checkout.service.postSaveHooks;

import com.homebase.ecom.checkout.domain.port.CheckoutEventPublisherPort;
import com.homebase.ecom.checkout.event.CheckoutEvent;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for Checkout post-save hooks that publish domain events.
 * Each subclass builds the appropriate CheckoutEvent and delegates to the single-method port.
 */
public abstract class AbstractCheckoutEventPostSaveHook implements PostSaveHook<Checkout> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected final CheckoutEventPublisherPort eventPublisher;

    protected AbstractCheckoutEventPostSaveHook(CheckoutEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    protected abstract CheckoutEvent buildEvent(Checkout checkout, TransientMap map);

    @Override
    public void execute(State startState, State endState, Checkout checkout, TransientMap map) {
        CheckoutEvent event = buildEvent(checkout, map);
        eventPublisher.publish(event);
    }
}
