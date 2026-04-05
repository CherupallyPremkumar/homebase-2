package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for IN_WAREHOUSE state.
 * Checks for low stock alerts when transitioning back from PARTIALLY_RESERVED.
 */
public class IN_WAREHOUSEInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(IN_WAREHOUSEInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public IN_WAREHOUSEInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (inventory.isLowStock()) {
            eventPublisher.publishLowStockAlert(inventory);
            log.warn("Published LowStockAlertEvent for productId={} at IN_WAREHOUSE state, available={}",
                    inventory.getProductId(), inventory.getAvailableQuantity());
        }
    }
}
