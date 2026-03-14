package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.chenile.workflow.param.MinimalPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action executed when supplier enters SUSPENDED state.
 * Ensures products are disabled.
 */
public class SupplierSuspendedAction extends AbstractSTMTransitionAction<Supplier, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SupplierSuspendedAction.class);

    @Override
    public void transitionTo(Supplier supplier, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now in SUSPENDED state. Products disabled.",
                supplier.getName(), supplier.getId());

        supplier.setProductsDisabled(true);
    }
}
