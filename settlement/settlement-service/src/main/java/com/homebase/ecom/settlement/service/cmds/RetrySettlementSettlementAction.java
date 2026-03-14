package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.RetrySettlementSettlementPayload;
import com.homebase.ecom.shared.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * Handles the retrySettlement event from FAILED state.
 * Clears previous calculation results so the settlement can be
 * re-processed from scratch in the PROCESSING state.
 */
public class RetrySettlementSettlementAction extends AbstractSTMTransitionAction<Settlement,
    RetrySettlementSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(RetrySettlementSettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement,
            RetrySettlementSettlementPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        log.info("Retrying settlement {} for supplier {}",
                settlement.getId(), settlement.getSupplierId());

        // Clear previous calculation so it can be re-done
        settlement.getLineItems().clear();
        settlement.setTotalSalesAmount(null);
        settlement.setCommissionAmount(null);
        settlement.setNetPayoutAmount(null);

        settlement.getTransientMap().previousPayload = payload;
    }
}
