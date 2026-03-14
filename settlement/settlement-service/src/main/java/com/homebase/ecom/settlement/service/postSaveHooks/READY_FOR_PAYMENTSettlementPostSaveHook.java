package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Post-save hook for READY_FOR_PAYMENT state.
 * Publishes an event indicating settlement calculation is complete and awaits admin payout confirmation.
 */
public class READY_FOR_PAYMENTSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(READY_FOR_PAYMENTSettlementPostSaveHook.class);

    @Autowired(required = false)
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} is READY_FOR_PAYMENT. Supplier: {}, Net payout: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetPayoutAmount());

        if (eventPublisher != null) {
            String netAmount = settlement.getNetPayoutAmount() != null
                    ? settlement.getNetPayoutAmount().toString()
                    : "N/A";
            eventPublisher.publishEvent(new SettlementStateChangeEvent(
                    settlement.getId(),
                    settlement.getSupplierId(),
                    startState != null ? startState.getStateId() : null,
                    "READY_FOR_PAYMENT",
                    "Settlement calculated, net payout: " + netAmount + ". Awaiting admin confirmation."));
        }
    }
}
