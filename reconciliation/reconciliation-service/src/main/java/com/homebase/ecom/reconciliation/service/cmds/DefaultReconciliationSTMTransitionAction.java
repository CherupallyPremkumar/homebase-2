package com.homebase.ecom.reconciliation.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.reconciliation.model.ReconciliationBatch;

/**
 * Default transition action invoked when no specific action is configured.
 */
public class DefaultReconciliationSTMTransitionAction<PayloadType extends MinimalPayload>
    extends AbstractSTMTransitionAction<ReconciliationBatch, PayloadType> {
    @Override
    public void transitionTo(ReconciliationBatch batch, PayloadType payload,
                 State startState, String eventId, State endState, STMInternalTransitionInvoker<?> stm,
                 Transition transition) {
    }
}
