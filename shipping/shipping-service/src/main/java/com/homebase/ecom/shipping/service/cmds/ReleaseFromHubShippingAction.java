package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.ReleaseFromHubShippingPayload;

import java.util.Date;

/**
 * Handles the releaseFromHub transition: HELD_AT_HUB -> DELIVERED.
 * Customer picks up from hub or carrier releases for delivery.
 */
public class ReleaseFromHubShippingAction extends AbstractSTMTransitionAction<Shipping,
        ReleaseFromHubShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            ReleaseFromHubShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Set actual delivery date
        shipping.setActualDeliveryDate(new Date());

        // Increment delivery attempts
        shipping.setDeliveryAttempts(shipping.getDeliveryAttempts() + 1);

        String pickedUpBy = payload.getPickedUpBy();
        if (pickedUpBy != null && !pickedUpBy.isEmpty()) {
            shipping.setCurrentLocation("Released from hub, picked up by " + pickedUpBy);
        } else {
            shipping.setCurrentLocation("Released from hub, delivered");
        }
    }
}
