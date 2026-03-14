package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.RepairDamagedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for repairing damaged stock in the warehouse.
 * Moves repaired units back to available inventory.
 */
public class RepairDamagedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, RepairDamagedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(RepairDamagedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            RepairDamagedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int repairedQty = payload.getRepairedQuantity() != null ? payload.getRepairedQuantity() : inventory.getDamagedQuantity();

        // Use domain method to repair damaged units (adds back to available)
        inventory.repairDamaged(repairedQty);
        inventory.setStatus(InventoryStatus.AVAILABLE);

        log.info("Repaired {} damaged units for productId={}, remaining damaged={}",
                repairedQty, inventory.getProductId(), inventory.getDamagedQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("repairedQuantity", repairedQty);
    }
}
