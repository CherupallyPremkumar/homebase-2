package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.RepairDamagedsInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for repairing damaged items during the PARTIAL_DAMAGE stage
 * (before warehouse allocation). Moves repaired units back to the approved pool.
 */
public class RepairDamagedsInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, RepairDamagedsInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(RepairDamagedsInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            RepairDamagedsInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int repairedQty = payload.getRepairedQuantity() != null ? payload.getRepairedQuantity() : inventory.getDamagedQuantity();

        inventory.repairDamagedPreAllocation(repairedQty, payload.getUnitIdentifiers());

        log.info("Repaired {} pre-allocation damaged units for productId={}, remaining damaged={}",
                repairedQty, inventory.getProductId(), inventory.getDamagedQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("repairedQuantity", repairedQty);
    }
}
