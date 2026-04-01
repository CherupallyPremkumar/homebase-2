package com.homebase.ecom.disputes.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.dto.EscalateDisputePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the escalate transition (OPEN -> ESCALATED, UNDER_REVIEW -> ESCALATED).
 * Sets priority to High and records escalation reason.
 */
public class EscalateDisputesAction extends AbstractSTMTransitionAction<Dispute,
        EscalateDisputePayload> {

    private static final Logger log = LoggerFactory.getLogger(EscalateDisputesAction.class);

    @Override
    public void transitionTo(Dispute dispute,
                             EscalateDisputePayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set priority to High on escalation
        dispute.priority = "High";

        // Override with payload priority if explicitly provided
        if (payload.getPriority() != null) {
            dispute.priority = payload.getPriority();
        }

        dispute.getTransientMap().previousPayload = payload;
        log.info("Dispute {} escalated from {} to ESCALATED. Priority={}, Reason: {}",
                dispute.getId(),
                startState.getId(),
                dispute.priority,
                payload.getEscalationReason() != null ? payload.getEscalationReason() : "Not specified");
    }
}
