package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.port.InventoryEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for STOCK_REJECTED state.
 * Publishes StockRejectedEvent to notify supplier management.
 */
public class STOCK_REJECTEDInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_REJECTEDInventoryItemPostSaveHook.class);

    private final InventoryEventPublisherPort eventPublisher;

    public STOCK_REJECTEDInventoryItemPostSaveHook(InventoryEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        String reason = (String) map.get("rejectionReason");
        eventPublisher.publishStockRejected(inventory, reason != null ? reason : "Quality issues");
        log.info("Published StockRejectedEvent for productId={}, reason={}", inventory.getProductId(), reason);
    }
}
