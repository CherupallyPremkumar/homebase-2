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
 * Handles the reject event: REVIEW_REQUIRED -> REJECTED.
 */
public class RejectReconciliationAction extends AbstractSTMTransitionAction<ReconciliationBatch, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectReconciliationAction.class);

    @Override
    public void transitionTo(ReconciliationBatch batch, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Rejecting reconciliation batch {}", batch.getId());
    }
}
