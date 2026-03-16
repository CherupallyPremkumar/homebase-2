package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.StockDepletedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for OUT_OF_STOCK state.
 * Publishes StockDepletedEvent to notify downstream systems.
 */
public class OUT_OF_STOCKInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(OUT_OF_STOCKInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        StockDepletedEvent event = new StockDepletedEvent(inventory.getId(), inventory.getProductId());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                    Map.of("key", inventory.getProductId(), "eventType", StockDepletedEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize StockDepletedEvent for productId={}", inventory.getProductId(), e);
            return;
        }
        log.warn("Published StockDepletedEvent for productId={}. Inventory is OUT_OF_STOCK.", inventory.getProductId());
    }
}
