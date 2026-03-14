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
 * Initial entry action for supplier flow.
 * Sets default values for newly created suppliers entering the state machine.
 */
public class SupplierFlowEntryAction extends AbstractSTMTransitionAction<Supplier, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(SupplierFlowEntryAction.class);

    @Override
    public void transitionTo(Supplier supplier, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Set default values for new suppliers
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            // Use description as fallback name if name is not set
            if (supplier.getDescription() != null && !supplier.getDescription().trim().isEmpty()) {
                supplier.setName(supplier.getDescription());
            } else {
                supplier.setName("New Supplier");
            }
        }

        supplier.setProductsDisabled(false);

        log.info("New supplier '{}' entering the supplier workflow", supplier.getName());
    }
}
