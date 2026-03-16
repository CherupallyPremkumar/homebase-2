package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.DisputeSettlementPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the dispute event.
 * Records the dispute reason for settlement review.
 */
public class DisputeSettlementAction extends AbstractSTMTransitionAction<Settlement, DisputeSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(DisputeSettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement, DisputeSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = (payload != null && payload.getDisputeReason() != null)
                ? payload.getDisputeReason()
                : "No reason provided";

        if (reason.isBlank()) {
            throw new IllegalArgumentException("Dispute must include a reason");
        }

        log.info("Settlement {} disputed by supplier {}. Reason: {}",
                settlement.getId(), settlement.getSupplierId(), reason);

        settlement.getTransientMap().previousPayload = payload;
    }
}
