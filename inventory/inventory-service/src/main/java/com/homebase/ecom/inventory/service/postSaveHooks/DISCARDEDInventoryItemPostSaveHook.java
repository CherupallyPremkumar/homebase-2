package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.StockDiscardedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for DISCARDED state.
 * Publishes StockDiscardedEvent for financial write-off tracking.
 */
public class DISCARDEDInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(DISCARDEDInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        StockDiscardedEvent event = new StockDiscardedEvent(inventory.getId(), inventory.getProductId(),
                inventory.getQuantity());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                    Map.of("key", inventory.getProductId(), "eventType", StockDiscardedEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize StockDiscardedEvent for productId={}", inventory.getProductId(), e);
            return;
        }
        log.info("Published StockDiscardedEvent for productId={}. Financial write-off required.",
                inventory.getProductId());
    }
}
