package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.DiscardDamagedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for discarding damaged inventory.
 * Behavior depends on transition context:
 * - From PARTIAL_DAMAGE -> IN_WAREHOUSE: discard damaged, allocate remaining to warehouse
 * - From DAMAGED_AT_WAREHOUSE -> DISCARDED: discard and write off
 */
public class DiscardDamagedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, DiscardDamagedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(DiscardDamagedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            DiscardDamagedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int discardQty = payload.getDiscardQuantity() != null ? payload.getDiscardQuantity() : inventory.getDamagedQuantity();

        // Discard the damaged units
        inventory.discardDamaged(discardQty);

        // If transitioning to IN_WAREHOUSE (from PARTIAL_DAMAGE), allocate remaining to warehouse
        if ("IN_WAREHOUSE".equals(endState.getStateId())) {
            // Remaining good stock becomes available in warehouse
            int remainingGood = inventory.getQuantity();
            inventory.setAvailableQuantity(remainingGood);
            inventory.setStatus(InventoryStatus.AVAILABLE);
            log.info("Discarded {} damaged units for productId={}, allocated {} good units to warehouse",
                    discardQty, inventory.getProductId(), remainingGood);
        } else {
            // Full discard (DAMAGED_AT_WAREHOUSE -> DISCARDED)
            log.info("Discarded {} damaged units for productId={}, remaining total={}",
                    discardQty, inventory.getProductId(), inventory.getQuantity());
        }

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("discardedQuantity", discardQty);
    }
}
