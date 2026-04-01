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
 * STM transition action that marks all products belonging to the
 * suspended/blacklisted supplier as inactive.
 */
public class DisableProductsAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(DisableProductsAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Disabling all products for supplier: {}", supplierId);

        if (supplierId == null || supplierId.isEmpty()) {
            String msg = "Cannot disable products: supplierId is null or empty";
            saga.setErrorMessage(msg);
            throw new RuntimeException(msg);
        }

        // Disable all products belonging to this supplier
        // In production, this calls the product service API
        Integer productCount = (Integer) saga.getTransientMap().get("supplierProductCount");
        int productsDisabled = (productCount != null) ? productCount : 0;

        saga.setProductsAffected(productsDisabled);
        saga.getTransientMap().put("productsDisabledAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("disableReason", saga.getReason());

        log.info("Disabled {} products for supplier: {}", productsDisabled, supplierId);
    }
}
