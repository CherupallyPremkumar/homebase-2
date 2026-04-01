package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ReactivateSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for reactivateSupplier event (SUSPENDED -> ACTIVE).
 * Clears suspension data, re-enables products, and updates active date.
 */
public class ReactivateSupplierAction extends AbstractSTMTransitionAction<Supplier, ReactivateSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReactivateSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier, ReactivateSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear suspension data
        supplier.setSuspensionReason(null);
        supplier.setSuspendedDate(null);

        // Re-enable products
        supplier.setProductsDisabled(false);

        // Update active date to track reactivation
        supplier.setActiveDate(LocalDateTime.now());

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) reactivated from {} state",
                supplier.getBusinessName(), supplier.getId(), startState.getStateId());
    }
}
