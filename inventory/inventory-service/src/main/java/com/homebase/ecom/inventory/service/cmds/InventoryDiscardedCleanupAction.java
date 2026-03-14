package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.chenile.workflow.param.MinimalPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exit action triggered when inventory reaches DISCARDED state.
 * Handles final cleanup: zeroes out all quantities and marks as discontinued.
 */
public class InventoryDiscardedCleanupAction extends AbstractSTMTransitionAction<InventoryItem, MinimalPayload> {

    private static final Logger log = LoggerFactory.getLogger(InventoryDiscardedCleanupAction.class);

    @Override
    public void transitionTo(InventoryItem inventory, MinimalPayload payload, State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Zero out all quantities
        inventory.setAvailableQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setDamagedQuantity(0);
        inventory.setStatus(InventoryStatus.DISCONTINUED);

        log.info("Inventory discarded and cleaned up for productId={}", inventory.getProductId());

        inventory.getTransientMap().put("stockDiscarded", true);
    }
}
