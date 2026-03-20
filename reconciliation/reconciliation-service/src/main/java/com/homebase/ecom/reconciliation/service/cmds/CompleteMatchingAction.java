package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the completeMatching event: PROCESSING -> CHECK_MISMATCHES.
 * Sets the unresolvedMismatchCount for the auto-state OGNL evaluation.
 */
public class CompleteMatchingAction extends AbstractSTMTransitionAction<ReconciliationBatch, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CompleteMatchingAction.class);

    @Override
    public void transitionTo(ReconciliationBatch batch, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set the unresolvedMismatchCount field for the CHECK_MISMATCHES auto-state
        batch.setUnresolvedMismatchCount(batch.getUnresolvedCount());

        log.info("Matching complete for batch {}. Matched: {}, Mismatched: {}, Unresolved: {}",
                batch.getId(), batch.getMatchedCount(), batch.getMismatchCount(), batch.getUnresolvedCount());
    }
}
