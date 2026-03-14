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
 * Post-save hook for PENDING state.
 * Publishes a SettlementCreatedEvent when a new settlement is created.
 */
public class PENDINGSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(PENDINGSettlementPostSaveHook.class);

    @Autowired(required = false)
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} created in PENDING state for supplier {}",
                settlement.getId(), settlement.getSupplierId());

        if (eventPublisher != null) {
            eventPublisher.publishEvent(new SettlementStateChangeEvent(
                    settlement.getId(),
                    settlement.getSupplierId(),
                    startState != null ? startState.getStateId() : null,
                    "PENDING",
                    "Settlement created"));
        }
    }
}
