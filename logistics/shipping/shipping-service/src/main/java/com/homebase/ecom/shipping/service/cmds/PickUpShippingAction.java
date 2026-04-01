package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.PickUpShippingPayload;

import java.util.Date;

/**
 * Handles the pickUp transition: LABEL_CREATED -> PICKED_UP.
 * Carrier picks up the package from warehouse.
 */
public class PickUpShippingAction extends AbstractSTMTransitionAction<Shipping,
        PickUpShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            PickUpShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update pickup location
        String location = payload.getPickupLocation();
        if (location != null && !location.isEmpty()) {
            shipping.setCurrentLocation("Picked up from " + location);
        } else {
            shipping.setCurrentLocation("Picked up by " + shipping.getCarrier());
        }
    }
}
