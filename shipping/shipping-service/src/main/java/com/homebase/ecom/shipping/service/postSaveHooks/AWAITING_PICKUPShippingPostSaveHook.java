package com.homebase.ecom.shipping.service.postSaveHooks;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.service.event.ShippingEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for AWAITING_PICKUP state.
 * Publishes ShipmentCreatedEvent after shipment is persisted.
 */
public class AWAITING_PICKUPShippingPostSaveHook implements PostSaveHook<Shipping> {

    @Autowired
    private ShippingEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        eventPublisher.publishShipmentCreated(shipping);
    }
}
