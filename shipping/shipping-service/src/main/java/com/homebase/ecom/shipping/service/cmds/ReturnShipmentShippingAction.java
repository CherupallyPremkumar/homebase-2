package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.ReturnShipmentShippingPayload;

/**
 * Handles the returnShipment transition: DELIVERY_FAILED -> RETURNED.
 * Warehouse decides to return shipment without further retry.
 */
public class ReturnShipmentShippingAction extends AbstractSTMTransitionAction<Shipping,
        ReturnShipmentShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            ReturnShipmentShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update location
        shipping.setCurrentLocation("Returned to warehouse");
    }
}
