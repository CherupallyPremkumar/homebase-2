package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.MarkLostShippingPayload;

/**
 * Handles the markLost transition: IN_TRANSIT -> LOST.
 * Package lost in transit, triggers insurance claim and order.deliveryFailed.
 */
public class MarkLostShippingAction extends AbstractSTMTransitionAction<Shipping,
        MarkLostShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            MarkLostShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        String lastKnown = payload.getLastKnownLocation();
        if (lastKnown != null && !lastKnown.isEmpty()) {
            shipping.setCurrentLocation("LOST - last known: " + lastKnown);
        } else {
            shipping.setCurrentLocation("LOST - location unknown");
        }
    }
}
