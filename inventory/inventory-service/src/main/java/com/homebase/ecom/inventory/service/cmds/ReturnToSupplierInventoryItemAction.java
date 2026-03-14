package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReturnToSupplierInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for initiating a return of damaged/rejected stock to the supplier.
 * Adjusts inventory quantities and tracks the return.
 */
public class ReturnToSupplierInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReturnToSupplierInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReturnToSupplierInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            ReturnToSupplierInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int returnQty = payload.getReturnQuantity() != null ? payload.getReturnQuantity() : inventory.getDamagedQuantity();

        // Use domain method to initiate supplier return
        inventory.initiateSupplierReturn(returnQty);

        log.info("Initiated return to supplier for productId={}, returnQty={}, reason={}",
                inventory.getProductId(), returnQty,
                payload.getReturnReason() != null ? payload.getReturnReason() : "damage");

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("returnQuantity", returnQty);
    }
}
