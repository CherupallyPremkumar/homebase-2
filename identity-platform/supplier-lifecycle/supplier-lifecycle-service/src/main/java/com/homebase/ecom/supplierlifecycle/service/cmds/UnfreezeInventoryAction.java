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
 * STM transition action that unfreezes inventory for the reactivated
 * supplier's products.
 */
public class UnfreezeInventoryAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(UnfreezeInventoryAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Unfreezing inventory for supplier: {}", supplierId);

        // Unfreeze all inventory items for the reactivated supplier
        Integer inventoryCount = (Integer) saga.getTransientMap().get("supplierInventoryCount");
        int inventoryUnfrozen = (inventoryCount != null) ? inventoryCount : saga.getProductsAffected();

        saga.setInventoryFrozen(inventoryUnfrozen);
        saga.getTransientMap().put("inventoryUnfrozenAt", java.time.Instant.now().toString());

        log.info("Unfroze {} inventory items for supplier: {}", inventoryUnfrozen, supplierId);
    }
}
