package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.TerminateSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for terminateSupplier event (any eligible state -> TERMINATED).
 * Records termination reason and date, permanently disables products.
 * This is a terminal state -- no further transitions are possible.
 */
public class TerminateSupplierAction extends AbstractSTMTransitionAction<Supplier, TerminateSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(TerminateSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, TerminateSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            reason = payload.getComment();
        }

        supplier.setTerminationReason(reason);
        supplier.setTerminatedDate(LocalDateTime.now());
        supplier.setProductsDisabled(true);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) terminated permanently. Reason: {}",
                supplier.getBusinessName(), supplier.getId(), reason);
    }
}
