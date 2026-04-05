package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for OUT_OF_STOCK state.
 * Publishes StockDepletedEvent to notify downstream systems.
 */
public class OUT_OF_STOCKInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(OUT_OF_STOCKInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public OUT_OF_STOCKInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        eventPublisher.publishStockDepleted(inventory);
        log.warn("Published StockDepletedEvent for productId={}. Inventory is OUT_OF_STOCK.", inventory.getProductId());
    }
}
