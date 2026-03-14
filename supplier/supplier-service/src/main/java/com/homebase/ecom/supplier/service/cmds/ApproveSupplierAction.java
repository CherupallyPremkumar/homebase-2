package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ApproveSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for approveSupplier event (PENDING_REVIEW -> ACTIVE).
 * Validates that supplier has minimum required information,
 * sets the active date, and optionally sets the commission percentage.
 */
public class ApproveSupplierAction extends AbstractSTMTransitionAction<Supplier, ApproveSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, ApproveSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate that supplier has minimum required fields for activation
        if (supplier.getName() == null || supplier.getName().trim().isEmpty()) {
            throw new IllegalStateException("Cannot approve supplier without a business name.");
        }

        // Set the active date
        supplier.setActiveDate(LocalDateTime.now());

        // Clear any previous rejection reason from a prior rejection cycle
        supplier.setRejectionReason(null);

        // Set commission percentage if provided in payload, otherwise use default
        if (payload.getCommissionPercentage() != null) {
            supplier.setCommissionPercentage(payload.getCommissionPercentage());
        } else if (supplier.getCommissionPercentage() == null) {
            supplier.setCommissionPercentage(10.0); // default platform commission
        }

        // Re-enable products if they were previously disabled
        supplier.setProductsDisabled(false);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) approved and transitioning to ACTIVE",
                supplier.getName(), supplier.getId());
    }
}
