package com.homebase.ecom.inventory.service.postSaveHooks;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.shared.event.DamageDetectedEvent;
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
 * Post save hook for DAMAGED_AT_WAREHOUSE state.
 * Publishes damage detected event for warehouse damage.
 */
public class DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private ChenilePub chenilePub;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (chenilePub == null) return;

        Integer damagedQty = (Integer) map.get("damagedQuantity");
        if (damagedQty != null) {
            DamageDetectedEvent event = new DamageDetectedEvent(inventory.getId(), inventory.getProductId(),
                    damagedQty, inventory.getDamagePercentage(), false);
            try {
                String body = objectMapper.writeValueAsString(event);
                chenilePub.publish(KafkaTopics.INVENTORY_EVENTS, body,
                        Map.of("key", inventory.getProductId(), "eventType", DamageDetectedEvent.EVENT_TYPE));
            } catch (JacksonException e) {
                log.error("Failed to serialize DamageDetectedEvent for productId={}", inventory.getProductId(), e);
                return;
            }
            log.info("Published DamageDetectedEvent (warehouse) for productId={}, damagedQty={}",
                    inventory.getProductId(), damagedQty);
        }
    }
}
