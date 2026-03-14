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
 * Post save hook for RETURNED_TO_SUPPLIER state.
 * Publishes event to notify settlement module for payment adjustments.
 */
public class RETURNED_TO_SUPPLIERInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(RETURNED_TO_SUPPLIERInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private InventoryEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (eventPublisher == null) return;

        eventPublisher.publishStockReturnedToSupplier(inventory);
        log.info("Published StockReturnedToSupplierEvent for productId={}. Settlement adjustment pending.",
                inventory.getProductId());
    }
}
