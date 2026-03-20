package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Handles the approve event: RESOLVED -> APPROVED or APPROVED -> COMPLETED.
 */
public class ApproveReconciliationAction extends AbstractSTMTransitionAction<ReconciliationBatch, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveReconciliationAction.class);

    @Override
    public void transitionTo(ReconciliationBatch batch, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Approving reconciliation batch {}", batch.getId());

        if ("COMPLETED".equals(endState.toString())) {
            batch.setCompletedAt(LocalDateTime.now());
        }
    }
}
