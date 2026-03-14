package com.homebase.ecom.supplierlifecycle.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action that retries a failed saga by resetting it
 * to the INITIATED state.
 */
public class RetryAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetryAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Retrying supplier lifecycle saga for supplier: {}, retry count: {}",
                saga.getSupplierId(), saga.getRetryCount() + 1);

        saga.setRetryCount(saga.getRetryCount() + 1);
        saga.setErrorMessage(null);
    }
}
