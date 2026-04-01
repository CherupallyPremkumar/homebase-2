package com.homebase.ecom.settlement.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.dto.DisburseSettlementPayload;
import com.homebase.ecom.settlement.domain.port.PayoutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the disburse event.
 * Initiates payout via PayoutPort and records disbursement reference.
 */
public class DisburseSettlementAction extends AbstractSTMTransitionAction<Settlement, DisburseSettlementPayload> {

    private static final Logger log = LoggerFactory.getLogger(DisburseSettlementAction.class);

    @Autowired(required = false)
    private PayoutPort payoutPort;

    @Override
    public void transitionTo(Settlement settlement, DisburseSettlementPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (settlement.getNetAmount() == null) {
            throw new IllegalArgumentException("Settlement must be calculated before disbursement");
        }

        String reference;
        if (payoutPort != null) {
            reference = payoutPort.initiatePayout(settlement);
        } else {
            reference = "MANUAL-" + System.currentTimeMillis();
        }

        settlement.setDisbursementReference(reference);

        log.info("Settlement {} disbursed to supplier {}. Reference: {}, Net amount: {}",
                settlement.getId(), settlement.getSupplierId(), reference, settlement.getNetAmount());

        settlement.getTransientMap().previousPayload = payload;
    }
}
