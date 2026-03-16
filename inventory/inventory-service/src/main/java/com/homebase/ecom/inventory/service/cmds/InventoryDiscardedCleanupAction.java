package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.State;
import org.chenile.stm.action.STMAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit action triggered when inventory reaches DISCARDED state.
 * Handles final cleanup: zeroes out all quantities and marks as discontinued.
 */
public class InventoryDiscardedCleanupAction implements STMAction<InventoryItem> {

    private static final Logger log = LoggerFactory.getLogger(InventoryDiscardedCleanupAction.class);

    @Override
    public void execute(State fromState, State toState, InventoryItem inventory) throws Exception {
        // Zero out all quantities
        inventory.setAvailableQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setDamagedQuantity(0);
        inventory.setStatus(InventoryStatus.DISCONTINUED);

        log.info("Inventory discarded and cleaned up for productId={}", inventory.getProductId());

        inventory.getTransientMap().put("stockDiscarded", true);
    }
}
