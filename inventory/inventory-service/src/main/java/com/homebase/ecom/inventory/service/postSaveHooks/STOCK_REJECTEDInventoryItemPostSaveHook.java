package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.StockRejectedEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for STOCK_REJECTED state.
 * Publishes StockRejectedEvent to notify supplier management.
 */
public class STOCK_REJECTEDInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_REJECTEDInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        String reason = (String) map.get("rejectionReason");
        StockRejectedEvent event = new StockRejectedEvent(inventory.getId(), inventory.getProductId(),
                reason != null ? reason : "Quality issues");
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                    Map.of("key", inventory.getProductId(), "eventType", StockRejectedEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize StockRejectedEvent for productId={}", inventory.getProductId(), e);
            return;
        }
        log.info("Published StockRejectedEvent for productId={}, reason={}", inventory.getProductId(), reason);
    }
}
