package com.homebase.ecom.supplier.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.dto.PutOnProbationPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * Transition action for putOnProbation event (ACTIVE -> ON_PROBATION).
 * Places supplier on probation due to poor performance metrics.
 */
public class PutOnProbationAction extends AbstractSTMTransitionAction<Supplier, PutOnProbationPayload> {

    private static final Logger log = LoggerFactory.getLogger(PutOnProbationAction.class);

    @Override
    public void transitionTo(Supplier supplier, PutOnProbationPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = payload.getReason();
        if (reason == null || reason.trim().isEmpty()) {
            reason = payload.getComment();
        }
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Performance metrics below threshold";
        }

        supplier.setProbationReason(reason);
        supplier.setProbationDate(LocalDateTime.now());

        supplier.getTransientMap().previousPayload = payload;

        log.info("Supplier '{}' (ID: {}) placed on probation. Reason: {}. Rating: {}, FulfillmentRate: {}",
                supplier.getBusinessName(), supplier.getId(), reason,
                supplier.getRating(), supplier.getFulfillmentRate());
    }
}
