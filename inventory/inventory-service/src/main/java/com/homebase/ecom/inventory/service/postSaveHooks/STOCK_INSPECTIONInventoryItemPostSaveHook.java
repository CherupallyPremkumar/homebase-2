package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for STOCK_INSPECTION state.
 * Logs the start of inspection for audit trail.
 */
public class STOCK_INSPECTIONInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_INSPECTIONInventoryItemPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        log.info("Stock inspection started for productId={}, qty={}",
                inventory.getProductId(), inventory.getQuantity());
    }
}
