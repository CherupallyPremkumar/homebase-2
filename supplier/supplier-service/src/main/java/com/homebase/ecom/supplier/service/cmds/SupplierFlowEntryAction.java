package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import com.homebase.ecom.supplier.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initial entry action for supplier flow.
 * Sets default values for newly created suppliers entering the state machine.
 */
public class SupplierFlowEntryAction implements STMAction<Supplier> {

    private static final Logger log = LoggerFactory.getLogger(SupplierFlowEntryAction.class);

    @Override
    public void execute(State fromState, State toState, Supplier supplier) throws Exception {

        // Set default business name if not provided
        if (supplier.getBusinessName() == null || supplier.getBusinessName().trim().isEmpty()) {
            supplier.setBusinessName("New Supplier");
        }

        // Initialize performance metrics
        supplier.setProductsDisabled(false);
        if (supplier.getFulfillmentRate() == 0.0) {
            supplier.setFulfillmentRate(100.0);
        }

        log.info("New supplier '{}' entering the supplier workflow", supplier.getBusinessName());
    }
}
