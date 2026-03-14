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
 * Exit action for BLACKLISTED state.
 * Ensures all products for this supplier are permanently disabled.
 * This is an additional safety measure -- the blacklist action and entry action
 * also disable products, but this exit action ensures the flag persists.
 */
public class DisableAllProductsAction extends AbstractSTMTransitionAction<Supplier, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(DisableAllProductsAction.class);

    @Override
    public void transitionTo(Supplier supplier, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        log.info("Disabling all products for blacklisted supplier '{}' (ID: {})",
                supplier.getName(), supplier.getId());

        supplier.setProductsDisabled(true);
    }
}
