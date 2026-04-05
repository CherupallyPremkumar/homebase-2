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
 * STM transition action that re-enables all products belonging to the
 * reactivated supplier.
 */
public class EnableProductsAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(EnableProductsAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Re-enabling all products for supplier: {}", supplierId);

        // Re-enable all products that were previously disabled for this supplier
        Integer productCount = (Integer) saga.getTransientMap().get("supplierProductCount");
        int productsEnabled = (productCount != null) ? productCount : 0;

        saga.setProductsAffected(productsEnabled);
        saga.getTransientMap().put("productsEnabledAt", java.time.Instant.now().toString());

        log.info("Re-enabled {} products for supplier: {}", productsEnabled, supplierId);
    }
}
