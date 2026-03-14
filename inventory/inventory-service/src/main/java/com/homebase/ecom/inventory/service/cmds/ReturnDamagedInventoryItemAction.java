package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReturnDamagedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action when damaged stock is discovered while stored in the warehouse.
 * Records the warehouse-level damage and adjusts available quantity.
 */
public class ReturnDamagedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReturnDamagedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReturnDamagedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            ReturnDamagedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int damagedQty = payload.getDamagedQuantity() != null ? payload.getDamagedQuantity() : 0;

        // Use domain method to record warehouse-level damage (adjusts available)
        inventory.recordWarehouseDamage(damagedQty);

        log.info("Warehouse damage found for productId={}: {} units damaged, available now {}",
                inventory.getProductId(), damagedQty, inventory.getAvailableQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("damagedQuantity", damagedQty);
    }
}
