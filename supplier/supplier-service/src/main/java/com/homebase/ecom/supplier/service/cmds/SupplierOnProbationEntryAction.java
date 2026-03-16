package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry action for ON_PROBATION state.
 * Supplier is under performance review. Products remain enabled but monitored.
 */
public class SupplierOnProbationEntryAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierOnProbationEntryAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {
        log.info("Supplier '{}' (ID: {}) is now ON_PROBATION. Rating: {}, FulfillmentRate: {}%",
                supplier.getBusinessName(), supplier.getId(),
                supplier.getRating(), supplier.getFulfillmentRate());

        // Products stay enabled during probation but supplier is under monitoring
    }
}
