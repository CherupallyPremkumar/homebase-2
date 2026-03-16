package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.AllocateToWarehouseInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action for allocating approved stock to a warehouse/fulfillment center.
 * Sets the available quantity and assigns the fulfillment center.
 */
public class AllocateToWarehouseInventoryItemAction
        extends AbstractSTMTransitionAction<InventoryItem, AllocateToWarehouseInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(AllocateToWarehouseInventoryItemAction.class);

    @Autowired
    private InventoryItemPolicyValidator policyValidator;

    @Override
    public void transitionTo(InventoryItem inventory,
            AllocateToWarehouseInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Determine quantity to allocate: from payload or the entire existing quantity
        int allocateQty = payload.getQuantity() != null ? payload.getQuantity() : inventory.getQuantity();

        // Policy enforcement: max warehouse capacity
        policyValidator.validateStockAllocation(inventory, allocateQty);

        // Set the fulfillment center
        if (payload.getWarehouseId() != null) {
            inventory.setPrimaryFulfillmentCenter(payload.getWarehouseId());
        }

        // Use domain method to add stock and set available quantity
        inventory.addStock(allocateQty, null, "Warehouse allocation to " +
                (payload.getWarehouseId() != null ? payload.getWarehouseId() : "default"));
        inventory.setInboundQuantity(0);

        log.info("Stock allocated to warehouse for productId={}, qty={}, warehouse={}",
                inventory.getProductId(), allocateQty,
                payload.getWarehouseId() != null ? payload.getWarehouseId() : "default");

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("allocatedQuantity", allocateQty);
    }
}
