package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.CancelShipmentShippingPayload;

/**
 * Handles the cancelShipment transition: PENDING/LABEL_CREATED -> CANCELLED.
 * Cancels shipment before carrier pickup.
 */
public class CancelShipmentShippingAction extends AbstractSTMTransitionAction<Shipping,
        CancelShipmentShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            CancelShipmentShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update location to cancelled
        shipping.setCurrentLocation("Shipment cancelled");
    }
}
