package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action for TERMINATED state.
 * Permanently disables all products.
 * This is a terminal state -- no further transitions are possible.
 */
public class SupplierTerminatedEntryAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierTerminatedEntryAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now TERMINATED. All products permanently disabled.",
                supplier.getBusinessName(), supplier.getId());

        supplier.setProductsDisabled(true);
    }
}
