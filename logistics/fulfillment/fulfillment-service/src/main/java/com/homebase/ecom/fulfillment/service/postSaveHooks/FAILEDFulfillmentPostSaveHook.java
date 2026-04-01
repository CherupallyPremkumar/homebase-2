package com.homebase.ecom.fulfillment.service.postSaveHooks;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post-save hook executed when fulfillment saga enters FAILED state.
 * Publishes FULFILLMENT_FAILED event for alerting and compensation.
 */
public class FAILEDFulfillmentPostSaveHook implements PostSaveHook<FulfillmentSaga> {

    private final FulfillmentEventPublisherPort eventPublisher;

    public FAILEDFulfillmentPostSaveHook(FulfillmentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, FulfillmentSaga saga, TransientMap map) {
        eventPublisher.publishFailed(saga, startState != null ? startState.toString() : "UNKNOWN");
    }
}
