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
 * Handles the retry event: REJECTED/FAILED -> CREATED.
 * Resets counters so the batch can be re-processed.
 */
public class RetryReconciliationAction extends AbstractSTMTransitionAction<ReconciliationBatch, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryReconciliationAction.class);

    @Override
    public void transitionTo(ReconciliationBatch batch, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Retrying reconciliation batch {}", batch.getId());

        // Reset counters
        batch.setMatchedCount(0);
        batch.setMismatchCount(0);
        batch.setAutoResolvedCount(0);
        batch.setUnresolvedCount(0);
        batch.setErrorMessage(null);
        batch.setCompletedAt(null);
    }
}
