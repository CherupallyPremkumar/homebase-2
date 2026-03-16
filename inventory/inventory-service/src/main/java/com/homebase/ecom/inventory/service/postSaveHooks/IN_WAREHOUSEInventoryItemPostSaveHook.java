package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.LowStockAlertEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for IN_WAREHOUSE state.
 * Checks for low stock alerts when transitioning back from PARTIALLY_RESERVED.
 */
public class IN_WAREHOUSEInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(IN_WAREHOUSEInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        // Check if we should alert on low stock after releasing reserved stock
        if (inventory.isLowStock()) {
            LowStockAlertEvent event = new LowStockAlertEvent(inventory.getId(), inventory.getProductId(),
                    inventory.getAvailableQuantity(), inventory.getLowStockThreshold());
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                        Map.of("key", inventory.getProductId(), "eventType", LowStockAlertEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize LowStockAlertEvent for productId={}", inventory.getProductId(), e);
                return;
            }
            log.warn("Published LowStockAlertEvent for productId={} at IN_WAREHOUSE state, available={}",
                    inventory.getProductId(), inventory.getAvailableQuantity());
        }
    }
}
