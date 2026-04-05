package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for RETURNED_TO_SUPPLIER state.
 * Publishes event to notify settlement module for payment adjustments.
 */
public class RETURNED_TO_SUPPLIERInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(RETURNED_TO_SUPPLIERInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public RETURNED_TO_SUPPLIERInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        eventPublisher.publishStockReturnedToSupplier(inventory);
        log.info("Published StockReturnedToSupplierEvent for productId={}. Settlement adjustment pending.",
                inventory.getProductId());
    }
}
