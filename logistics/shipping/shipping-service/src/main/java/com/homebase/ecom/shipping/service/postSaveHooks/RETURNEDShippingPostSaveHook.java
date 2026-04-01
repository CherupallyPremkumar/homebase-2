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
 * PostSaveHook for RETURNED state.
 * Publishes RETURNED event — notifies customer, signals order for refund.
 */
public class RETURNEDShippingPostSaveHook implements PostSaveHook<Shipping> {

    private static final Logger log = LoggerFactory.getLogger(RETURNEDShippingPostSaveHook.class);

    @Autowired
    private ShippingEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        eventPublisher.publishReturned(shipping);
        log.info("Shipment {} for order {} has been returned to warehouse.",
                shipping.getId(), shipping.getOrderId());
    }
}
