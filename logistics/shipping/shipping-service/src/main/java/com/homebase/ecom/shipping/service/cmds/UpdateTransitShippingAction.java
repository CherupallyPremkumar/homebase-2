package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.UpdateTransitShippingPayload;

import java.util.Calendar;

/**
 * Handles the updateTransit transition: PICKED_UP/IN_TRANSIT -> IN_TRANSIT.
 * Updates tracking status with current location and recalculates ETA.
 */
public class UpdateTransitShippingAction extends AbstractSTMTransitionAction<Shipping,
        UpdateTransitShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            UpdateTransitShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update current location if provided
        String location = payload.getCurrentLocation();
        if (location != null && !location.isEmpty()) {
            shipping.setCurrentLocation(location);
        } else {
            shipping.setCurrentLocation("In transit - sorting facility");
        }

        // Recalculate ETA if updated days provided
        int updatedEtaDays = payload.getUpdatedEtaDays();
        if (updatedEtaDays > 0) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, updatedEtaDays);
            shipping.setEstimatedDeliveryDate(cal.getTime());
        }
    }
}
