package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.OutForDeliveryShippingPayload;

import java.util.Calendar;

/**
 * Handles the outForDelivery transition: IN_TRANSIT -> OUT_FOR_DELIVERY.
 * Final mile delivery has started.
 */
public class OutForDeliveryShippingAction extends AbstractSTMTransitionAction<Shipping,
        OutForDeliveryShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            OutForDeliveryShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Update location to local hub or delivery area
        String localHub = payload.getLocalHub();
        if (localHub != null && !localHub.isEmpty()) {
            shipping.setCurrentLocation("Out for delivery from " + localHub);
        } else {
            shipping.setCurrentLocation("Out for delivery - local hub");
        }

        // Estimated delivery is today
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        shipping.setEstimatedDeliveryDate(cal.getTime());
    }
}
