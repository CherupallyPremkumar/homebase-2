package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;

/**
 * Entry-action for PICKED_UP state.
 * Updates location to indicate package has been collected by courier.
 */
public class PickedUpAction implements STMTransitionAction<Shipping> {
    @Override
    public void doTransition(Shipping shipping, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Update location to picked up status
        if (shipping.getCurrentLocation() == null ||
                shipping.getCurrentLocation().contains("awaiting")) {
            shipping.setCurrentLocation("Picked up by " + shipping.getCarrier());
        }
    }
}
