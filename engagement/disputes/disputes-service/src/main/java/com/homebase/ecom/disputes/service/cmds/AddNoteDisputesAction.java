package com.homebase.ecom.disputes.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.disputes.model.Dispute;
import com.homebase.ecom.disputes.dto.AddNoteDisputePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the addNote transition (self-transition, stays in same state).
 * Appends note to the dispute's activity log.
 */
public class AddNoteDisputesAction extends AbstractSTMTransitionAction<Dispute,
        AddNoteDisputePayload> {

    private static final Logger log = LoggerFactory.getLogger(AddNoteDisputesAction.class);

    @Override
    public void transitionTo(Dispute dispute,
                             AddNoteDisputePayload payload,
                             State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Append note to activities
        if (payload.getNote() != null) {
            dispute.addActivity("addNote", payload.getNote());
        }

        dispute.getTransientMap().previousPayload = payload;
        log.info("Note added to dispute {} in state {}",
                dispute.getId(), startState.getId());
    }
}
