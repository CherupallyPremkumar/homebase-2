package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.PickupInitiatedReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the pickupInitiated transition (APPROVED -> IN_TRANSIT_BACK).
 * Records pickup tracking number for the return shipment.
 */
public class PickupInitiatedReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        PickupInitiatedReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(PickupInitiatedReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             PickupInitiatedReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Record tracking number
        if (payload.getPickupTrackingNumber() != null) {
            returnrequest.pickupTrackingNumber = payload.getPickupTrackingNumber();
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} pickup initiated with tracking: {}",
                returnrequest.getId(), returnrequest.pickupTrackingNumber);
    }
}
