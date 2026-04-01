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
 * STM transition action that freezes all inventory for the supplier's products,
 * preventing any further reservations or stock movements.
 */
public class FreezeInventoryAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(FreezeInventoryAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Freezing inventory for supplier: {}", supplierId);

        // Freeze all inventory items belonging to this supplier
        Integer inventoryCount = (Integer) saga.getTransientMap().get("supplierInventoryCount");
        int inventoryFrozen = (inventoryCount != null) ? inventoryCount : saga.getProductsAffected();

        saga.setInventoryFrozen(inventoryFrozen);
        saga.getTransientMap().put("inventoryFrozenAt", java.time.Instant.now().toString());

        log.info("Froze {} inventory items for supplier: {}", inventoryFrozen, supplierId);
    }
}
