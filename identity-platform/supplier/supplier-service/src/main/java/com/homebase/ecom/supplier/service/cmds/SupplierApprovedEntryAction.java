package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action for APPROVED state.
 * Supplier has been approved and is completing onboarding.
 */
public class SupplierApprovedEntryAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierApprovedEntryAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now APPROVED, completing onboarding",
                supplier.getBusinessName(), supplier.getId());
    }
}
