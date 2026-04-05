package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.RejectSettlementPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the reject event. Marks the settlement as FAILED.
 */
public class RejectSettlementAction extends AbstractSTMTransitionAction<Settlement, RejectSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(RejectSettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement, RejectSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String reason = (payload != null && payload.getComment() != null)
                ? payload.getComment()
                : "Settlement rejected";

        log.info("Settlement {} rejected for supplier {}. Reason: {}",
                settlement.getId(), settlement.getSupplierId(), reason);

        settlement.getTransientMap().previousPayload = payload;
    }
}
