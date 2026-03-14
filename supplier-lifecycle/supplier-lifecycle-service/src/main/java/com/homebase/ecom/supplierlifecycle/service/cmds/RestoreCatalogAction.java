package com.homebase.ecom.supplierlifecycle.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.param.MinimalPayload;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM transition action that restores catalog entries for the reactivated
 * supplier's products.
 */
public class RestoreCatalogAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RestoreCatalogAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Restoring catalog entries for supplier: {}", supplierId);

        // Restore catalog entries for the re-enabled products
        int productsAffected = saga.getProductsAffected();
        Integer catalogMultiplier = (Integer) saga.getTransientMap().get("catalogEntriesPerProduct");
        int multiplier = (catalogMultiplier != null) ? catalogMultiplier : 1;
        int entriesRestored = productsAffected * multiplier;

        saga.setCatalogEntriesRemoved(entriesRestored);
        saga.getTransientMap().put("catalogRestoredAt", java.time.Instant.now().toString());

        log.info("Restored {} catalog entries for supplier: {}", entriesRestored, supplierId);
    }
}
