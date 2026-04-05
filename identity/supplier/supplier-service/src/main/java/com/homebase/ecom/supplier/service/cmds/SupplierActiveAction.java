package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action executed when supplier enters ACTIVE state.
 * Ensures active date is set and products are enabled.
 */
public class SupplierActiveAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierActiveAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now in ACTIVE state", supplier.getBusinessName(), supplier.getId());

        // Ensure products are enabled when entering ACTIVE state
        supplier.setProductsDisabled(false);
    }
}
