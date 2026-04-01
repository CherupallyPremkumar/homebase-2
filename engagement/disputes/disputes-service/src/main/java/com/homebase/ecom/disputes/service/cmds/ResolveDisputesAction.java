package com.homebase.ecom.disputes.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.dto.ResolveDisputePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handles the resolve transition (UNDER_REVIEW -> RESOLVED, ESCALATED -> RESOLVED).
 * Sets resolutionOutcome, refundAmount, resolutionNotes, and resolutionDate.
 */
public class ResolveDisputesAction extends AbstractSTMTransitionAction<Dispute,
        ResolveDisputePayload> {

    private static final Logger log = LoggerFactory.getLogger(ResolveDisputesAction.class);

    @Override
    public void transitionTo(Dispute dispute,
                             ResolveDisputePayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set resolution outcome
        if (payload.getResolutionOutcome() != null) {
            dispute.resolutionOutcome = payload.getResolutionOutcome();
        }

        // Set refund amount
        if (payload.getRefundAmount() != null) {
            dispute.refundAmount = payload.getRefundAmount();
        }

        // Set resolution notes
        if (payload.getResolutionNotes() != null) {
            dispute.resolutionNotes = payload.getResolutionNotes();
        }

        // Set resolution date
        dispute.resolutionDate = LocalDateTime.now();

        dispute.getTransientMap().previousPayload = payload;
        log.info("Dispute {} resolved from {}. Outcome={}, refundAmount={}",
                dispute.getId(),
                startState.getId(),
                dispute.resolutionOutcome,
                dispute.refundAmount);
    }
}
