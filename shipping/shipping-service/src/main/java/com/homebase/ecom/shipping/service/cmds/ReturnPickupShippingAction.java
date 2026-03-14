package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.ReturnPickupShippingPayload;

import java.util.UUID;

/**
 * Handles the returnPickup transition: RETURN_REQUESTED -> IN_TRANSIT.
 * Courier picks up return item and starts return transit.
 */
public class ReturnPickupShippingAction extends AbstractSTMTransitionAction<Shipping,
        ReturnPickupShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            ReturnPickupShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update return carrier if provided
        String returnCarrier = payload.getReturnCarrier();
        if (returnCarrier != null && !returnCarrier.isEmpty()) {
            shipping.setCarrier(returnCarrier);
        }

        // Update return tracking number if provided
        String returnTracking = payload.getReturnTrackingNumber();
        if (returnTracking != null && !returnTracking.isEmpty()) {
            shipping.setReturnTrackingNumber(returnTracking);
        } else if (shipping.getReturnTrackingNumber() == null) {
            shipping.setReturnTrackingNumber("RTN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Update location
        shipping.setCurrentLocation("Return picked up - in transit to warehouse");
    }
}
