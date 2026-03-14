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
 * Post-save hook for PROCESSING state.
 * Publishes an event indicating settlement processing has started (or restarted via retry).
 */
public class PROCESSINGSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(PROCESSINGSettlementPostSaveHook.class);

    @Autowired(required = false)
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        String fromStateId = startState != null ? startState.getStateId() : "NONE";
        boolean isRetry = "FAILED".equals(fromStateId);

        log.info("Settlement {} entered PROCESSING state from {} for supplier {} (period {}/{}). Retry={}",
                settlement.getId(), fromStateId, settlement.getSupplierId(),
                settlement.getPeriodMonth(), settlement.getPeriodYear(), isRetry);

        if (eventPublisher != null) {
            String desc = isRetry
                    ? "Settlement retry initiated, re-entering PROCESSING"
                    : "Settlement processing started for period " + settlement.getPeriodMonth() + "/" + settlement.getPeriodYear();
            eventPublisher.publishEvent(new SettlementStateChangeEvent(
                    settlement.getId(),
                    settlement.getSupplierId(),
                    fromStateId,
                    "PROCESSING",
                    desc));
        }
    }
}
