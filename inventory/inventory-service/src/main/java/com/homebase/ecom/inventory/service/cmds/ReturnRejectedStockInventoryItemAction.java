package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReturnRejectedStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for returning rejected stock to the supplier.
 * Initiates the return process for the entire rejected batch.
 */
public class ReturnRejectedStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReturnRejectedStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReturnRejectedStockInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            ReturnRejectedStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Initiate return for the full rejected quantity
        int returnQty = inventory.getQuantity();
        inventory.initiateSupplierReturn(returnQty);

        log.info("Returning rejected stock to supplier for productId={}, qty={}",
                inventory.getProductId(), returnQty);

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("returnQuantity", returnQty);
    }
}
