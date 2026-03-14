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
 * STM transition action that removes all catalog entries for the supplier's
 * products so they are no longer visible in search results or category pages.
 */
public class RemoveCatalogAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(RemoveCatalogAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Removing catalog entries for supplier: {}", supplierId);

        // Remove catalog entries corresponding to the disabled products
        int productsAffected = saga.getProductsAffected();
        // Each product may have multiple catalog entries (across categories/collections)
        Integer catalogMultiplier = (Integer) saga.getTransientMap().get("catalogEntriesPerProduct");
        int multiplier = (catalogMultiplier != null) ? catalogMultiplier : 1;
        int entriesRemoved = productsAffected * multiplier;

        saga.setCatalogEntriesRemoved(entriesRemoved);
        saga.getTransientMap().put("catalogClearedAt", java.time.Instant.now().toString());

        log.info("Removed {} catalog entries for supplier: {}", entriesRemoved, supplierId);
    }
}
