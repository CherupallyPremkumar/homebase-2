package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.ResolveFromProbationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for resolveFromProbation event (ON_PROBATION -> ACTIVE).
 * Clears probation data and restores supplier to full active status.
 */
public class ResolveFromProbationAction extends AbstractSTMTransitionAction<Supplier, ResolveFromProbationPayload> {

    private static final Logger log = LoggerFactory.getLogger(ResolveFromProbationAction.class);

    @Override
    public void transitionTo(Supplier supplier, ResolveFromProbationPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Clear probation data
        supplier.setProbationReason(null);
        supplier.setProbationDate(null);

        // Update active date
        supplier.setActiveDate(LocalDateTime.now());

        // Ensure products are enabled
        supplier.setProductsDisabled(false);

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) resolved from probation, now ACTIVE again",
                supplier.getBusinessName(), supplier.getId());
    }
}
