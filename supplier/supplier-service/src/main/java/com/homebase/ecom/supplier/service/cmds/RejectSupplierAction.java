package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.RejectSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transition action for rejectSupplier event (PENDING_REVIEW -> REJECTED).
 * Requires a rejection reason to be provided.
 */
public class RejectSupplierAction extends AbstractSTMTransitionAction<Supplier, RejectSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, RejectSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate that rejection reason is provided
        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            // Fall back to comment if reason not set explicitly
            reason = payload.getComment();
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalStateException("Rejection reason is required when rejecting a supplier.");
        }

        supplier.setRejectionReason(reason);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) rejected. Reason: {}",
                supplier.getName(), supplier.getId(), reason);
    }
}
