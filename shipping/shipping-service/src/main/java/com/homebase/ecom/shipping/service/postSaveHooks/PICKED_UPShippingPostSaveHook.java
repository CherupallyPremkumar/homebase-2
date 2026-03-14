package com.homebase.ecom.shipping.service.postSaveHooks;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.service.event.ShippingEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * PostSaveHook for PICKED_UP state.
 * Logs pickup confirmation.
 */
public class PICKED_UPShippingPostSaveHook implements PostSaveHook<Shipping> {

    private static final Logger log = LoggerFactory.getLogger(PICKED_UPShippingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        log.info("Shipment {} picked up by carrier {}. Tracking: {}",
                shipping.getId(), shipping.getCarrier(), shipping.getTrackingNumber());
    }
}
