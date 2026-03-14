package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.SoldAllReservedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action when all reserved stock for an order is sold/fulfilled.
 * Confirms the reservation and transitions to OUT_OF_STOCK if inventory is depleted.
 */
public class SoldAllReservedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, SoldAllReservedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(SoldAllReservedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            SoldAllReservedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Use domain method to confirm reservation (marks as FULFILLED, deducts from quantity)
        if (payload.getOrderId() != null) {
            inventory.confirmReservation(payload.getOrderId());
        }

        // Set status to out of stock
        inventory.setAvailableQuantity(0);
        inventory.setStatus(InventoryStatus.OUT_OF_STOCK);

        log.info("All reserved stock sold for productId={}, orderId={}. Inventory is now OUT_OF_STOCK.",
                inventory.getProductId(), payload.getOrderId());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("stockDepleted", true);
    }
}
