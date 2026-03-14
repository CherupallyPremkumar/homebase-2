package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.ReturnRequestedShippingPayload;

import java.util.UUID;

/**
 * Handles the returnRequested transition: DELIVERED/IN_TRANSIT -> RETURN_REQUESTED.
 * Creates return tracking number and records return reason.
 */
public class ReturnRequestedShippingAction extends AbstractSTMTransitionAction<Shipping,
        ReturnRequestedShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            ReturnRequestedShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Record return reason
        String reason = payload.getReturnReason();
        if (reason != null && !reason.isEmpty()) {
            shipping.setReturnReason(reason);
        } else {
            shipping.setReturnReason("Customer requested return");
        }

        // Generate return tracking number
        shipping.setReturnTrackingNumber("RTN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Update location
        shipping.setCurrentLocation("Return requested - awaiting pickup");
    }
}
