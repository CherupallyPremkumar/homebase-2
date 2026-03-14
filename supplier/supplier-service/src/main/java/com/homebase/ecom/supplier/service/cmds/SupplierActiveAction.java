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
 * Entry action executed when supplier enters ACTIVE state.
 * Ensures active date is set and products are enabled.
 */
public class SupplierActiveAction extends AbstractSTMTransitionAction<Supplier, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SupplierActiveAction.class);

    @Override
    public void transitionTo(Supplier supplier, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now in ACTIVE state", supplier.getName(), supplier.getId());

        // Ensure products are enabled when entering ACTIVE state
        supplier.setProductsDisabled(false);
    }
}
