package com.homebase.ecom.settlement.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.settlement.model.Settlement;
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
 * Post-save hook for DISPUTED state.
 * Publishes SETTLEMENT_DISPUTED event to Kafka.
 */
public class DISPUTEDSettlementPostSaveHook implements PostSaveHook<Settlement> {

    private static final Logger log = LoggerFactory.getLogger(DISPUTEDSettlementPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, Settlement settlement, TransientMap map) {
        log.warn("Settlement {} DISPUTED by supplier {}",
                settlement.getId(), settlement.getSupplierId());

        if (chenilePub != null) {
            try {
                SettlementStateChangeEvent event = new SettlementStateChangeEvent(
                        settlement.getId(),
                        settlement.getSupplierId(),
                        startState != null ? startState.getStateId() : null,
                        "DISPUTED",
                        "Settlement disputed by supplier");
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.SETTLEMENT_EVENTS, body,
                        Map.of("key", settlement.getId(), "eventType", "SETTLEMENT_DISPUTED"));
            } catch (JacksonException e) {
                log.error("Failed to publish SETTLEMENT_DISPUTED event for settlement {}", settlement.getId(), e);
            }
        }
    }
}
