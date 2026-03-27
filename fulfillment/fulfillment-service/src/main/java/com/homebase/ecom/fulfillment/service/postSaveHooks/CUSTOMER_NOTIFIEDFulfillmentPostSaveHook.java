package com.homebase.ecom.fulfillment.service.postSaveHooks;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post-save hook executed when fulfillment saga enters CUSTOMER_NOTIFIED state.
 * Publishes CUSTOMER_NOTIFIED event.
 */
public class CUSTOMER_NOTIFIEDFulfillmentPostSaveHook implements PostSaveHook<FulfillmentSaga> {

    private final FulfillmentEventPublisherPort eventPublisher;

    public CUSTOMER_NOTIFIEDFulfillmentPostSaveHook(FulfillmentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, FulfillmentSaga saga, TransientMap map) {
        eventPublisher.publishCustomerNotified(saga);
    }
}
