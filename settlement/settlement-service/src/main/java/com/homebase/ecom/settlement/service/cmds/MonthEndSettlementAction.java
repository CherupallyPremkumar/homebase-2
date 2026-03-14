package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.MonthEndSettlementPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the monthEnd event which triggers settlement processing for a supplier.
 * Validates that the settlement has the required supplier and period information,
 * then transitions it to PROCESSING state.
 */
public class MonthEndSettlementAction extends AbstractSTMTransitionAction<Settlement,
    MonthEndSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(MonthEndSettlementAction.class);

    @Override
    public void transitionTo(Settlement settlement,
            MonthEndSettlementPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate that the settlement has required supplier context
        if (settlement.getSupplierId() == null || settlement.getSupplierId().isBlank()) {
            throw new IllegalArgumentException("Settlement must have a supplierId set before month-end trigger");
        }

        if (settlement.getPeriodMonth() == null || settlement.getPeriodYear() == null) {
            throw new IllegalArgumentException("Settlement must have periodMonth and periodYear set before month-end trigger");
        }

        log.info("Month-end settlement triggered for supplier: {}, period: {}/{}",
                settlement.getSupplierId(), settlement.getPeriodMonth(), settlement.getPeriodYear());

        settlement.getTransientMap().previousPayload = payload;
    }
}
