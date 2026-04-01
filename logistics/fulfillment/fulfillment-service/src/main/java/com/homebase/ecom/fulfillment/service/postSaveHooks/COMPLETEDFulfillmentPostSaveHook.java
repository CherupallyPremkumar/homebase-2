package com.homebase.ecom.fulfillment.service.postSaveHooks;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post-save hook executed when fulfillment saga enters COMPLETED state.
 * Publishes FULFILLMENT_COMPLETED event for order BC.
 */
public class COMPLETEDFulfillmentPostSaveHook implements PostSaveHook<FulfillmentSaga> {

    private final FulfillmentEventPublisherPort eventPublisher;

    public COMPLETEDFulfillmentPostSaveHook(FulfillmentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, FulfillmentSaga saga, TransientMap map) {
        eventPublisher.publishCompleted(saga);
    }
}
