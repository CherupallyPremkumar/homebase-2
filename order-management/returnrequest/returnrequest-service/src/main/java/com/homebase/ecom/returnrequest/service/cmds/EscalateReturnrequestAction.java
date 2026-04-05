package com.homebase.ecom.returnrequest.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.returnrequest.model.Returnrequest;
import com.homebase.ecom.returnrequest.dto.EscalateReturnPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the escalate transition (UNDER_REVIEW -> ESCALATED).
 * Records escalation reason for supervisor review.
 */
public class EscalateReturnrequestAction extends AbstractSTMTransitionAction<Returnrequest,
        EscalateReturnPayload> {

    private static final Logger log = LoggerFactory.getLogger(EscalateReturnrequestAction.class);

    @Override
    public void transitionTo(Returnrequest returnrequest,
                             EscalateReturnPayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        returnrequest.getTransientMap().previousPayload = payload;
        log.info("Return request {} escalated. Reason: {}",
                returnrequest.getId(),
                payload.getEscalationReason() != null ? payload.getEscalationReason() : "Not specified");
    }
}
