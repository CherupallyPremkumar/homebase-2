package com.homebase.ecom.checkout.service.impl;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.service.handler.CheckoutExternalEventHandler;
import org.chenile.stm.STM;
import org.chenile.stm.impl.STMActionsInfoProvider;
import org.chenile.utils.entity.service.EntityStore;
import org.chenile.workflow.service.impl.HmStateEntityServiceImpl;

/**
 * Checkout service implementation extending Chenile's HmStateEntityServiceImpl.
 * Adds external event handling for cross-BC payment events.
 */
public class CheckoutServiceImpl extends HmStateEntityServiceImpl<Checkout> {

    private final CheckoutExternalEventHandler externalEventHandler;

    public CheckoutServiceImpl(STM<Checkout> stm, STMActionsInfoProvider stmActionsInfoProvider,
                               EntityStore<Checkout> entityStore,
                               CheckoutExternalEventHandler externalEventHandler) {
        super(stm, stmActionsInfoProvider, entityStore);
        this.externalEventHandler = externalEventHandler;
    }

    /**
     * Handles incoming cross-BC events (payment.events).
     * Parses the event, identifies the checkout, and triggers the appropriate STM transition.
     */
    public void onExternalEvent(String eventPayload) {
        externalEventHandler.handle(eventPayload, this);
    }
}
