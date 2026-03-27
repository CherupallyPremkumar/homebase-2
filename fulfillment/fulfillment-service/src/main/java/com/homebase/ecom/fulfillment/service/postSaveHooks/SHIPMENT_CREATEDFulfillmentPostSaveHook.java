package com.homebase.ecom.fulfillment.service.postSaveHooks;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.port.FulfillmentEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;

/**
 * Post-save hook executed when fulfillment saga enters SHIPMENT_CREATED state.
 * Publishes SHIPMENT_CREATED event for downstream consumers.
 */
public class SHIPMENT_CREATEDFulfillmentPostSaveHook implements PostSaveHook<FulfillmentSaga> {

    private final FulfillmentEventPublisherPort eventPublisher;

    public SHIPMENT_CREATEDFulfillmentPostSaveHook(FulfillmentEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, FulfillmentSaga saga, TransientMap map) {
        eventPublisher.publishShipmentCreated(saga);
    }
}
