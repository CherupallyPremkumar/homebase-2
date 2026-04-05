package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReturnStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for returning stock from an order return.
 * Adds returned quantity back to available inventory.
 */
public class ReturnStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReturnStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReturnStockInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory, ReturnStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int returnQty = payload.getQuantity() != null ? payload.getQuantity() : 0;

        inventory.addStock(returnQty, payload.getOrderId(),
                "Order return: " + (payload.getReturnReason() != null ? payload.getReturnReason() : "customer return"));

        log.info("Stock returned to warehouse for productId={}, orderId={}, qty={}, newAvailable={}",
                inventory.getProductId(), payload.getOrderId(), returnQty, inventory.getAvailableQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("returnedQuantity", returnQty);
    }
}
