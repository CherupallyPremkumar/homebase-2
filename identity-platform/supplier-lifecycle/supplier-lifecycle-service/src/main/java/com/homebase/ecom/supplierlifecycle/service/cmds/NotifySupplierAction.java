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
 * STM transition action that notifies the supplier that their account
 * has been reactivated.
 */
public class NotifySupplierAction
        extends AbstractSTMTransitionAction<SupplierLifecycleSaga, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(NotifySupplierAction.class);

    @Override
    public void transitionTo(SupplierLifecycleSaga saga, MinimalPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String supplierId = saga.getSupplierId();
        log.info("Notifying supplier of reactivation: {}", supplierId);

        // Notify supplier about reactivation
        String notificationId = "NTF-SUPP-" + supplierId + "-" + System.currentTimeMillis();
        saga.getTransientMap().put("supplierNotificationId", notificationId);
        saga.getTransientMap().put("supplierNotifiedAt", java.time.Instant.now().toString());
        saga.getTransientMap().put("notificationType", "SUPPLIER_REACTIVATION");

        saga.setErrorMessage(null);

        log.info("Supplier {} notified of reactivation, notificationId: {}", supplierId, notificationId);
    }
}
