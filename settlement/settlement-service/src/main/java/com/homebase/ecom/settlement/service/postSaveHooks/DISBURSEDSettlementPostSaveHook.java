package com.homebase.ecom.settlement.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.domain.port.NotificationPort;
import com.homebase.ecom.shared.event.KafkaTopics;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post-save hook for DISBURSED state.
 * Notifies the supplier, triggers reconciliation, and publishes SETTLEMENT_DISBURSED event.
 */
public class DISBURSEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(DISBURSEDSettlementPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

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
        if (chenilePub != null) {
            try {
                SettlementStateChangeEvent event = new SettlementStateChangeEvent(
                        settlement.getId(),
                        settlement.getSupplierId(),
                        startState != null ? startState.getStateId() : null,
                        "DISBURSED",
                        "Settlement disbursed. Ref: " + settlement.getDisbursementReference()
                                + ", Amount: " + settlement.getNetAmount());
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.SETTLEMENT_EVENTS, body,
                        Map.of("key", settlement.getId(), "eventType", "SETTLEMENT_DISBURSED"));
            } catch (JacksonException e) {
                log.error("Failed to publish SETTLEMENT_DISBURSED event for settlement {}", settlement.getId(), e);
            }
        }

        // Trigger reconciliation log
        log.info("Reconciliation initiated for settlement {} with reference {}",
                settlement.getId(), settlement.getDisbursementReference());
    }
}
