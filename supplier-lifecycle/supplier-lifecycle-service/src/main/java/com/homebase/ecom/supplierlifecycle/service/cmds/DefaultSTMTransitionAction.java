package com.homebase.ecom.supplierlifecycle.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;

/**
 * Default transition action invoked when no specific transition action is specified.
 */
public class DefaultSTMTransitionAction<PayloadType extends MinimalPayload>
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, PayloadType> {

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, PayloadType payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) {
    }
}
