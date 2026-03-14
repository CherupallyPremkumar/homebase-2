package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisher;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Post save hook for DAMAGED_AT_WAREHOUSE state.
 * Publishes damage detected event for warehouse damage.
 */
public class DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(DAMAGED_AT_WAREHOUSEInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private InventoryEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (eventPublisher == null) return;

        Integer damagedQty = (Integer) map.get("damagedQuantity");
        if (damagedQty != null) {
            eventPublisher.publishDamageDetected(inventory, damagedQty, false);
            log.info("Published DamageDetectedEvent (warehouse) for productId={}, damagedQty={}",
                    inventory.getProductId(), damagedQty);
        }
    }
}
