package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.RestockArriveInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action when new stock arrives for restock, starting a new stock cycle.
 * Resets the inventory quantities for the incoming batch.
 */
public class RestockArriveInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, RestockArriveInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(RestockArriveInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            RestockArriveInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int newQty = payload.getQuantity() != null ? payload.getQuantity() : 0;

        // Reset for new stock cycle
        inventory.resetForRestock(newQty);

        log.info("Restock arrived for productId={}, newQty={}. New stock cycle started.",
                inventory.getProductId(), newQty);

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("restockQuantity", newQty);
    }
}
