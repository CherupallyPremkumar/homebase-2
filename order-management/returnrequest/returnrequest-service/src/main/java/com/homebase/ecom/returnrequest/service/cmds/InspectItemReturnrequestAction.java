package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.InspectItemReturnrequestPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the inspectItem transition (ITEM_RECEIVED -> INSPECTED).
 * Warehouse inspector verifies the returned item condition.
 */
public class InspectItemReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        InspectItemReturnrequestPayload> {

    private static final Logger log = LoggerFactory.getLogger(InspectItemReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             InspectItemReturnrequestPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getInspectorId() != null) {
            returnrequest.inspectorId = payload.getInspectorId();
        }
        if (payload.getInspectorNotes() != null) {
            returnrequest.inspectorNotes = payload.getInspectorNotes();
        }

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} inspected by {}, condition: {}",
                returnrequest.getId(), returnrequest.inspectorId, payload.getVerifiedCondition());
    }
}
