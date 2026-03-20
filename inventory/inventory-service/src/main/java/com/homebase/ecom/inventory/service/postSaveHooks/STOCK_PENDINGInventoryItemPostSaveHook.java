package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.RestockArrivedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for STOCK_PENDING state.
 * Publishes restock arrived event when transitioning from OUT_OF_STOCK.
 */
public class STOCK_PENDINGInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_PENDINGInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        // Publish restock event if coming from OUT_OF_STOCK
        Integer restockQty = (Integer) map.get("restockQuantity");
        if (startState != null && "OUT_OF_STOCK".equals(startState.getStateId()) && restockQty != null) {
            RestockArrivedEvent event = new RestockArrivedEvent(inventory.getId(), inventory.getProductId(), restockQty);
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                        Map.of("key", inventory.getProductId() != null ? inventory.getProductId() : inventory.getId(), "eventType", RestockArrivedEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize RestockArrivedEvent for productId={}", inventory.getProductId(), e);
                return;
            }
            log.info("Published RestockArrivedEvent for productId={}, qty={}",
                    inventory.getProductId(), restockQty);
        }
    }
}
