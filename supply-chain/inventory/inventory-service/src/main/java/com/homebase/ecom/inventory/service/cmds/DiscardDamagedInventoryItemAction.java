package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.DiscardDamagedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for discarding damaged inventory.
 * Updates damage record statuses and adjusts quantities — XML auto-states handle routing.
 */
public class DiscardDamagedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, DiscardDamagedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(DiscardDamagedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            DiscardDamagedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int discardQty = payload.getDiscardQuantity() != null ? payload.getDiscardQuantity() : inventory.getDamagedQuantity();

        inventory.discardDamaged(discardQty, payload.getUnitIdentifiers());

        log.info("Discarded {} damaged units for productId={}, remainingQty={}",
                discardQty, inventory.getProductId(), inventory.getQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("discardedQuantity", discardQty);
    }
}
