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
 * Post-save hook for APPROVED state.
 * Notifies the supplier and publishes SETTLEMENT_CALCULATED event.
 */
public class APPROVEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(APPROVEDSettlementPostSaveHook.class);

    @Autowired(required = false)
    private NotificationPort notificationPort;

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.info("Settlement {} APPROVED for supplier {}. Net amount: {}",
                settlement.getId(), settlement.getSupplierId(), settlement.getNetAmount());

        // Notify supplier
        if (notificationPort != null) {
            notificationPort.notifySettlementApproved(settlement);
        }

        // Publish SETTLEMENT_CALCULATED event to Kafka
        if (chenilePub != null) {
            try {
                SettlementStateChangeEvent event = new SettlementStateChangeEvent(
                        settlement.getId(),
                        settlement.getSupplierId(),
                        startState != null ? startState.getStateId() : null,
                        "APPROVED",
                        "Settlement calculated and approved. Net: " + settlement.getNetAmount());
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.SETTLEMENT_EVENTS, body,
                        Map.of("key", settlement.getId(), "eventType", "SETTLEMENT_CALCULATED"));
            } catch (JacksonException e) {
                log.error("Failed to publish SETTLEMENT_CALCULATED event for settlement {}", settlement.getId(), e);
            }
        }
    }
}
