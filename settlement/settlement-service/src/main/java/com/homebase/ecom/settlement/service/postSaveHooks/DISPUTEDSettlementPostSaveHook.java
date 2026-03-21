package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.domain.port.SettlementEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for DISPUTED state.
 * Publishes SETTLEMENT_DISPUTED event to Kafka.
 */
public class DISPUTEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(DISPUTEDSettlementPostSaveHook.class);

    private final SettlementEventPublisherPort eventPublisher;

    public DISPUTEDSettlementPostSaveHook(SettlementEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.warn("Settlement {} DISPUTED by supplier {}",
                settlement.getId(), settlement.getSupplierId());

        String fromState = startState != null ? startState.getStateId() : null;
        eventPublisher.publishSettlementDisputed(settlement, fromState);
    }
}
