package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.chenile.workflow.param.MinimalPayload;

/**
 * STM action for checking and updating inventory status based on current quantities.
 * Recalculates the InventoryStatus based on available quantity and thresholds.
 */
public class CheckInventoryItemStatusAction extends AbstractSTMTransitionAction<InventoryItem, MinimalPayload> {

    @Override
    public void transitionTo(InventoryItem inventory, MinimalPayload payload, State startState, String eventId,
                             State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Recalculate status based on quantities
        if (inventory.isOutOfStock()) {
            inventory.setStatus(InventoryStatus.OUT_OF_STOCK);
        } else if (inventory.isLowStock()) {
            inventory.setStatus(InventoryStatus.LOW_STOCK);
        } else {
            inventory.setStatus(InventoryStatus.AVAILABLE);
        }

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("currentStatus", inventory.getStatus().name());
    }
}
