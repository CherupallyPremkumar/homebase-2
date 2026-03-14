package com.homebase.ecom.supplier.service.postSaveHooks;

import com.homebase.ecom.supplier.model.Supplier;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for INACTIVE state.
 * Supplier has self-paused. Products are hidden but can be re-enabled on reactivation.
 * No external event published since this is a supplier-initiated action.
 */
public class INACTIVESupplierPostSaveHook implements PostSaveHook<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(INACTIVESupplierPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Supplier supplier, TransientMap map) {
        log.info("PostSaveHook: Supplier '{}' (ID: {}) is now INACTIVE (self-paused). Products hidden.",
                supplier.getName(), supplier.getId());
    }
}
