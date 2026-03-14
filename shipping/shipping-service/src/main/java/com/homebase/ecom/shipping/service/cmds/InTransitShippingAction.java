package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.InTransitShippingPayload;

import java.util.Calendar;
import java.util.Date;

/**
 * Handles the inTransit transition: PICKED_UP -> IN_TRANSIT.
 * Updates tracking status with current location and recalculates ETA.
 */
public class InTransitShippingAction extends AbstractSTMTransitionAction<Shipping,
        InTransitShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            InTransitShippingPayload payload,
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
            shipping.setEstimatedDelivery(cal.getTime());
        }

        // Ensure shippedAt is set (in case it was missed)
        if (shipping.getShippedAt() == null) {
            shipping.setShippedAt(new Date());
        }
    }
}
