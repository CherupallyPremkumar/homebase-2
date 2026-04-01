package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.ApproveSettlementPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the approve event.
 * Validates settlement has been calculated before approval.
 */
public class ApproveSettlementAction extends AbstractSTMTransitionAction<Settlement, ApproveSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveSettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement, ApproveSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (settlement.getNetAmount() == null) {
            throw new IllegalArgumentException("Settlement must be calculated before approval");
        }

        log.info("Settlement {} approved for supplier {}. Net amount: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetAmount());

        settlement.getTransientMap().previousPayload = payload;
    }
}
