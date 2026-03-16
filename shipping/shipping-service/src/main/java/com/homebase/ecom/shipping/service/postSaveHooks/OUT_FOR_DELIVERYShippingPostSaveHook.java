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
 * PostSaveHook for OUT_FOR_DELIVERY state.
 * Publishes OUT_FOR_DELIVERY event and notifies customer.
 */
public class OUT_FOR_DELIVERYShippingPostSaveHook implements PostSaveHook<Shipping> {

    private static final Logger log = LoggerFactory.getLogger(OUT_FOR_DELIVERYShippingPostSaveHook.class);

    @Autowired
    private ShippingEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        eventPublisher.publishOutForDelivery(shipping);
        log.info("Shipment {} is out for delivery for order {}. Estimated delivery: {}",
                shipping.getId(), shipping.getOrderId(), shipping.getEstimatedDeliveryDate());
    }
}
