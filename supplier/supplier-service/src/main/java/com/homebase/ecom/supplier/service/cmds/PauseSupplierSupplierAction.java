package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.PauseSupplierSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transition action for pauseSupplier event (ACTIVE -> INACTIVE).
 * Supplier-initiated pause. Products are temporarily hidden but not disabled.
 */
public class PauseSupplierSupplierAction extends AbstractSTMTransitionAction<Supplier,
    PauseSupplierSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(PauseSupplierSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier,
            PauseSupplierSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Mark products as disabled during pause
        supplier.setProductsDisabled(true);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) paused by supplier request",
                supplier.getName(), supplier.getId());
    }
}
