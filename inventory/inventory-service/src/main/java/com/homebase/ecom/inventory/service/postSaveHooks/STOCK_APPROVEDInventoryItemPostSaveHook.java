package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for STOCK_APPROVED state.
 * Logs the approval for audit purposes.
 */
public class STOCK_APPROVEDInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(STOCK_APPROVEDInventoryItemPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        Integer approvedQty = (Integer) map.get("approvedQuantity");
        if (approvedQty != null) {
            log.info("Stock approved for productId={}, qty={}", inventory.getProductId(), approvedQty);
        }
    }
}
