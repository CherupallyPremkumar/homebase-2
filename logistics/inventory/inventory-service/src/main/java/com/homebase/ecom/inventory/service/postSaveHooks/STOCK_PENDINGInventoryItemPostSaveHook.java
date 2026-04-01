package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for STOCK_PENDING state.
 * Publishes restock arrived event when transitioning from OUT_OF_STOCK.
 */
public class STOCK_PENDINGInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_PENDINGInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public STOCK_PENDINGInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        Integer restockQty = (Integer) map.get("restockQuantity");
        if (startState != null && "OUT_OF_STOCK".equals(startState.getStateId()) && restockQty != null) {
            eventPublisher.publishRestockArrived(inventory, restockQty);
            log.info("Published RestockArrivedEvent for productId={}, qty={}",
                    inventory.getProductId(), restockQty);
        }
    }
}
