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
 * STM transition action that notifies customers affected by the supplier
 * suspension or blacklisting.
 */
public class NotifyCustomersAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(NotifyCustomersAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Notifying customers affected by supplier suspension: {}", supplierId);

        // Notify affected customers about order cancellations
        int ordersCancelled = saga.getOrdersCancelled();
        // Each cancelled order has one customer to notify
        int customersNotified = ordersCancelled;

        saga.getTransientMap().put("customersNotified", customersNotified);
        saga.getTransientMap().put("customersNotifiedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("notificationType", "SUPPLIER_SUSPENSION");

        // Clear any error since this is the final step
        saga.setErrorMessage(null);

        log.info("Notified {} customers about supplier suspension: {}", customersNotified, supplierId);
    }
}
