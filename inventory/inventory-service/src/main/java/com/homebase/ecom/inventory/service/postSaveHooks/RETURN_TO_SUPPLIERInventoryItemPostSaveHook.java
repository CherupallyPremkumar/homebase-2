package com.homebase.ecom.inventory.service.postSaveHooks;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Post save hook for RETURN_TO_SUPPLIER state.
 * Logs the return initiation for tracking purposes.
 */
public class RETURN_TO_SUPPLIERInventoryItemPostSaveHook implements PostSaveHook<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(RETURN_TO_SUPPLIERInventoryItemPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, InventoryItem inventory, TransientMap map) {
        Integer returnQty = (Integer) map.get("returnQuantity");
        log.info("Return to supplier initiated for productId={}, returnQty={}",
                inventory.getProductId(), returnQty != null ? returnQty : "N/A");
    }
}
