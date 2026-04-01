package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ApproveSupplierPayload;
import com.homebase.ecom.supplier.service.validator.SupplierPolicyValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * Transition action for approveSupplier event (UNDER_REVIEW -> APPROVED).
 * Validates minimum required information and sets commission rate from cconfig.
 */
public class ApproveSupplierAction extends AbstractSTMTransitionAction<Supplier, ApproveSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveSupplierAction.class);

    @Autowired(required = false)
    private SupplierPolicyValidator policyValidator;

    @Override
    public void transitionTo(Supplier supplier, ApproveSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate that supplier has minimum required fields
        if (supplier.getBusinessName() == null || supplier.getBusinessName().trim().isEmpty()) {
            throw new IllegalStateException("Cannot approve supplier without a business name.");
        }

        // Validate rating meets minimum threshold if policy validator is available
        if (policyValidator != null) {
            policyValidator.validateForApproval(supplier);
        }

        // Set the active date
        supplier.setActiveDate(LocalDateTime.now());

        // Clear any previous rejection reason
        supplier.setRejectionReason(null);

        // Set commission rate from payload or cconfig default
        if (payload.getCommissionPercentage() != null) {
            supplier.setCommissionRate(payload.getCommissionPercentage());
        } else if (supplier.getCommissionRate() == 0.0) {
            double defaultRate = (policyValidator != null)
                    ? policyValidator.getCommissionRateDefault()
                    : 15.0;
            supplier.setCommissionRate(defaultRate);
        }

        // Initialize performance metrics for newly approved supplier
        if (supplier.getFulfillmentRate() == 0.0) {
            supplier.setFulfillmentRate(100.0); // Start at 100%
        }

        // Re-enable products
        supplier.setProductsDisabled(false);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) approved and transitioning to APPROVED",
                supplier.getBusinessName(), supplier.getId());
    }
}
