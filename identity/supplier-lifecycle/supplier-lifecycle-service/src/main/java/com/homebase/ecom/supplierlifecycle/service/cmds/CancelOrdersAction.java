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
 * STM transition action that cancels all pending orders containing items
 * from the suspended/blacklisted supplier.
 */
public class CancelOrdersAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(CancelOrdersAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Cancelling pending orders for supplier: {}", supplierId);

        // Cancel all pending orders containing items from this supplier
        Integer pendingOrderCount = (Integer) saga.getTransientMap().get("pendingOrderCount");
        int ordersCancelled = (pendingOrderCount != null) ? pendingOrderCount : 0;

        saga.setOrdersCancelled(ordersCancelled);
        saga.getTransientMap().put("ordersCancelledAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("cancellationReason", "Supplier " + saga.getAction().toLowerCase() + ": " + saga.getReason());

        log.info("Cancelled {} pending orders for supplier: {}", ordersCancelled, supplierId);
    }
}
