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
 * Entry action executed when supplier enters BLACKLISTED state.
 * This is a terminal state -- no further transitions are possible.
 * All products must be permanently disabled.
 */
public class SupplierBlacklistedAction extends AbstractSTMTransitionAction<Supplier, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SupplierBlacklistedAction.class);

    @Override
    public void transitionTo(Supplier supplier, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now BLACKLISTED. All products permanently disabled.",
                supplier.getName(), supplier.getId());

        supplier.setProductsDisabled(true);
    }
}
