package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.service.validator.InventoryItemPolicyValidator;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ApproveStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM action to mark stock as approved after warehouse inspection.
 * Validates the approved quantity meets minimum policy requirements,
 * then updates the inventory quantities accordingly.
 */
public class ApproveStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ApproveStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ApproveStockInventoryItemAction.class);

    @Autowired
    private InventoryItemPolicyValidator policyValidator;

    @Override
    public void transitionTo(InventoryItem inventory,
            ApproveStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int qty = payload.getQuantity() != null ? payload.getQuantity() : inventory.getQuantity();

        // Policy enforcement: minimum quantity to approve
        policyValidator.validateApprovalQuantity(inventory, qty);

        // Update the inventory with approved quantity
        inventory.setQuantity(qty);
        inventory.setInboundQuantity(0);

        log.info("Stock approved for productId={}, approvedQty={}", inventory.getProductId(), qty);

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("approvedQuantity", qty);
    }
}
