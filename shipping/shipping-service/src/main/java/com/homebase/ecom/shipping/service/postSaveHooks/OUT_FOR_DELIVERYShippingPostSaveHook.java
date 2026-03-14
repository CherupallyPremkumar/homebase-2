package com.homebase.ecom.shipping.service.postSaveHooks;

import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for OUT_FOR_DELIVERY state.
 * Logs out-for-delivery notification (could trigger SMS/push notification).
 */
public class OUT_FOR_DELIVERYShippingPostSaveHook implements PostSaveHook<Shipping> {

    private static final Logger log = LoggerFactory.getLogger(OUT_FOR_DELIVERYShippingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        log.info("Shipment {} is out for delivery for order {}. Estimated delivery: {}",
                shipping.getId(), shipping.getOrderId(), shipping.getEstimatedDelivery());
    }
}
