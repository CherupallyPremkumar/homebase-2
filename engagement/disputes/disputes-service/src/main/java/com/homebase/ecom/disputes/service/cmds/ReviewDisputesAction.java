package com.homebase.ecom.disputes.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.dto.ReviewDisputePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the review transition (OPEN -> UNDER_REVIEW).
 * Sets assignedTo and investigationNotes from payload.
 */
public class ReviewDisputesAction extends AbstractSTMTransitionAction<Dispute,
        ReviewDisputePayload> {

    private static final Logger log = LoggerFactory.getLogger(ReviewDisputesAction.class);

    @Override
    public void transitionTo(Dispute dispute,
                             ReviewDisputePayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Assign investigator
        if (payload.getAssignedTo() != null) {
            dispute.assignedTo = payload.getAssignedTo();
        }

        // Record investigation notes
        if (payload.getInvestigationNotes() != null) {
            dispute.investigationNotes = payload.getInvestigationNotes();
        }

        dispute.getTransientMap().previousPayload = payload;
        log.info("Dispute {} reviewed, assigned to {}, moving to UNDER_REVIEW",
                dispute.getId(), dispute.assignedTo);
    }
}
