package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ResubmitSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transition action for resubmitSupplier event (REJECTED -> APPLIED).
 * Clears previous rejection data so the supplier starts a fresh review cycle.
 */
public class ResubmitSupplierAction extends AbstractSTMTransitionAction<Supplier, ResubmitSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResubmitSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, ResubmitSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear previous rejection reason for a fresh review
        supplier.setRejectionReason(null);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) resubmitted for review after rejection",
                supplier.getBusinessName(), supplier.getId());
    }
}
