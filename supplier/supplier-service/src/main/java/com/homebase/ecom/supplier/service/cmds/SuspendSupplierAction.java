package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.SuspendSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for suspendSupplier event (ACTIVE/ON_PROBATION -> SUSPENDED).
 * Requires a suspension reason. Records suspension date and disables products.
 */
public class SuspendSupplierAction extends AbstractSTMTransitionAction<Supplier, SuspendSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(SuspendSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, SuspendSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            reason = payload.getComment();
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalStateException("Suspension reason is required when suspending a supplier.");
        }

        supplier.setSuspensionReason(reason);
        supplier.setSuspendedDate(LocalDateTime.now());
        supplier.setProductsDisabled(true);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) suspended. Reason: {}",
                supplier.getBusinessName(), supplier.getId(), reason);
    }
}
