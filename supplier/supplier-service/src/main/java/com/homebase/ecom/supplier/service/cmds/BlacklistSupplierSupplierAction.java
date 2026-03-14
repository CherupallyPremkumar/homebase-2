package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.BlacklistSupplierSupplierPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for blacklistSupplier event (any eligible state -> BLACKLISTED).
 * Records the blacklist reason and date, marks all products as disabled.
 * This is a permanent terminal state.
 */
public class BlacklistSupplierSupplierAction extends AbstractSTMTransitionAction<Supplier,
    BlacklistSupplierSupplierPayload> {

    private static final Logger log = LoggerFactory.getLogger(BlacklistSupplierSupplierAction.class);

    @Override
    public void transitionTo(Supplier supplier,
            BlacklistSupplierSupplierPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Extract reason from payload or comment
        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            reason = payload.getComment();
        }

        supplier.setBlacklistReason(reason);
        supplier.setBlacklistedDate(LocalDateTime.now());
        supplier.setProductsDisabled(true);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) blacklisted permanently. Reason: {}",
                supplier.getName(), supplier.getId(), reason);
    }
}
