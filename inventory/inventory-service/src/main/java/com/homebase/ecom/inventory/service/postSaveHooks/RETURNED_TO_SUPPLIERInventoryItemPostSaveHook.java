package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.KafkaTopics;
import com.homebase.ecom.shared.event.StockReturnedToSupplierEvent;
import org.chenile.pubsub.ChenilePub;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Post save hook for RETURNED_TO_SUPPLIER state.
 * Publishes event to notify settlement module for payment adjustments.
 */
public class RETURNED_TO_SUPPLIERInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(RETURNED_TO_SUPPLIERInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        StockReturnedToSupplierEvent event = new StockReturnedToSupplierEvent(inventory.getId(),
                inventory.getProductId(), inventory.getQuantity());
        try {
            String body = objectMapper.writeValueAsString(event);
            chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                    Map.of("key", inventory.getProductId() != null ? inventory.getProductId() : inventory.getId(), "eventType", StockReturnedToSupplierEvent.EVENT_TYPE));
        } catch (JacksonException e) {
            log.error("Failed to serialize StockReturnedToSupplierEvent for productId={}", inventory.getProductId(), e);
            return;
        }
        log.info("Published StockReturnedToSupplierEvent for productId={}. Settlement adjustment pending.",
                inventory.getProductId());
    }
}
