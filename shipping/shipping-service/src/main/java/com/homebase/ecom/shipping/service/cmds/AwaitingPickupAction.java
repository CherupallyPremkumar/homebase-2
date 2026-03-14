package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;

import java.util.UUID;

/**
 * Entry-action for AWAITING_PICKUP state.
 * Ensures tracking number is generated when shipment enters this initial state.
 */
public class AwaitingPickupAction implements STMTransitionAction<Shipping> {
    @Override
    public void doTransition(Shipping shipping, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Generate tracking number if not already present
        if (shipping.getTrackingNumber() == null || shipping.getTrackingNumber().isEmpty()) {
            shipping.setTrackingNumber("TRK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        // Set default carrier if not present
        if (shipping.getCarrier() == null || shipping.getCarrier().isEmpty()) {
            shipping.setCarrier("HOMEBASE-LOGISTICS");
        }

        // Set initial location
        shipping.setCurrentLocation("Warehouse - awaiting courier pickup");
    }
}
