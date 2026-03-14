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
 * Post save hook for STOCK_PENDING state.
 * Publishes restock arrived event when transitioning from OUT_OF_STOCK.
 */
public class STOCK_PENDINGInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_PENDINGInventoryItemPostSaveHook.class);

    @Autowired(required = false)
    private InventoryEventPublisher eventPublisher;

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        if (eventPublisher == null) return;

        // Publish restock event if coming from OUT_OF_STOCK
        Integer restockQty = (Integer) map.get("restockQuantity");
        if (startState != null && "OUT_OF_STOCK".equals(startState.getStateId()) && restockQty != null) {
            eventPublisher.publishRestockArrived(inventory, restockQty);
            log.info("Published RestockArrivedEvent for productId={}, qty={}",
                    inventory.getProductId(), restockQty);
        }
    }
}
