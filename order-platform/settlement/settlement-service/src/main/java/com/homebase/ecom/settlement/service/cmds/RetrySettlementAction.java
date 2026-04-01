package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.RetrySettlementPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the retry event from FAILED state.
 * Clears previous calculation results so the settlement can be re-processed.
 */
public class RetrySettlementAction extends AbstractSTMTransitionAction<Settlement, RetrySettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetrySettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement, RetrySettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Retrying settlement {} for supplier {}", settlement.getId(), settlement.getSupplierId());

        // Clear previous calculation so it can be re-done
        settlement.setCommissionAmount(null);
        settlement.setPlatformFee(null);
        settlement.setNetAmount(null);
        settlement.setDisbursementReference(null);

        settlement.getTransientMap().previousPayload = payload;
    }
}
