package com.homebase.ecom.shipping.service.postSaveHooks;

import com.homebase.ecom.shipping.model.Shipping;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for RETURNED state.
 * Logs completion of return process.
 */
public class RETURNEDShippingPostSaveHook implements PostSaveHook<Shipping> {

    private static final Logger log = LoggerFactory.getLogger(RETURNEDShippingPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Shipping shipping, TransientMap map) {
        log.info("Shipment {} for order {} has been returned to warehouse. Return tracking: {}",
                shipping.getId(), shipping.getOrderId(), shipping.getReturnTrackingNumber());
    }
}
