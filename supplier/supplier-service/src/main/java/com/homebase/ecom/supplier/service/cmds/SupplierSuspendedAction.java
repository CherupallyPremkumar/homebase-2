package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action executed when supplier enters SUSPENDED state.
 * Ensures products are disabled.
 */
public class SupplierSuspendedAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierSuspendedAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now in SUSPENDED state. Products disabled.",
                supplier.getBusinessName(), supplier.getId());

        supplier.setProductsDisabled(true);
    }
}
