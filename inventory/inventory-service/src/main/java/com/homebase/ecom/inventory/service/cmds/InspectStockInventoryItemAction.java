package com.homebase.ecom.inventory.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.dto.InspectStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action to move stock into inspection.
 * Sets the inbound quantity for tracking purposes.
 */
public class InspectStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, InspectStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(InspectStockInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            InspectStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Mark inbound quantity as the current total for inspection tracking
        inventory.setInboundQuantity(inventory.getQuantity());

        log.info("Stock inspection started for productId={}, quantity={}",
                inventory.getProductId(), inventory.getQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
    }
}
