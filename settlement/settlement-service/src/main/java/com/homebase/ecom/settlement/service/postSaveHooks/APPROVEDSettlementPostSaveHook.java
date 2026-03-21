package com.homebase.ecom.settlement.service.postSaveHooks;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.domain.port.NotificationPort;
import com.homebase.ecom.settlement.domain.port.SettlementEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post-save hook for APPROVED state.
 * Notifies the supplier and publishes SETTLEMENT_CALCULATED event.
 */
public class APPROVEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(APPROVEDSettlementPostSaveHook.class);

    private final NotificationPort notificationPort;
    private final SettlementEventPublisherPort eventPublisher;

    public APPROVEDSettlementPostSaveHook(NotificationPort notificationPort,
                                           SettlementEventPublisherPort eventPublisher) {
        this.notificationPort = notificationPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} APPROVED for supplier {}. Net amount: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetAmount());

        // Notify supplier
        if (notificationPort != null) {
            notificationPort.notifySettlementApproved(settlement);
        }

        // Publish SETTLEMENT_CALCULATED event to Kafka
        String fromState = startState != null ? startState.getStateId() : null;
        eventPublisher.publishSettlementApproved(settlement, fromState);
    }
}
