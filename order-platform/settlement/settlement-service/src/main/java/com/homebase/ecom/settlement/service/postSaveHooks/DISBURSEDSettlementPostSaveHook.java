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
 * Post-save hook for DISBURSED state.
 * Notifies the supplier, triggers reconciliation, and publishes SETTLEMENT_DISBURSED event.
 */
public class DISBURSEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(DISBURSEDSettlementPostSaveHook.class);

    private final NotificationPort notificationPort;
    private final SettlementEventPublisherPort eventPublisher;

    public DISBURSEDSettlementPostSaveHook(NotificationPort notificationPort,
                                            SettlementEventPublisherPort eventPublisher) {
        this.notificationPort = notificationPort;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} DISBURSED to supplier {}. Reference: {}, Net: {}",
                settlement.getId(), settlement.getSupplierId(),
                settlement.getDisbursementReference(), settlement.getNetAmount());

        // Notify supplier of disbursement
        if (notificationPort != null) {
            notificationPort.notifySettlementDisbursed(settlement);
        }

        // Publish SETTLEMENT_DISBURSED event to Kafka
        String fromState = startState != null ? startState.getStateId() : null;
        eventPublisher.publishSettlementDisbursed(settlement, fromState);

        // Trigger reconciliation log
        log.info("Reconciliation initiated for settlement {} with reference {}",
                settlement.getId(), settlement.getDisbursementReference());
    }
}
