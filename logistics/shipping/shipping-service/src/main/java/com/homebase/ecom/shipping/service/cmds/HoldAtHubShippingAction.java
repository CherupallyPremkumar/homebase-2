package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.HoldAtHubShippingPayload;

/**
 * Handles the holdAtHub transition: IN_TRANSIT -> HELD_AT_HUB.
 * Package held at delivery hub or locker for customer pickup.
 */
public class HoldAtHubShippingAction extends AbstractSTMTransitionAction<Shipping,
        HoldAtHubShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            HoldAtHubShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        String hubName = payload.getHubName();
        if (hubName != null && !hubName.isEmpty()) {
            shipping.setCurrentLocation("Held at hub: " + hubName);
        } else {
            shipping.setCurrentLocation("Held at delivery hub");
        }
    }
}
