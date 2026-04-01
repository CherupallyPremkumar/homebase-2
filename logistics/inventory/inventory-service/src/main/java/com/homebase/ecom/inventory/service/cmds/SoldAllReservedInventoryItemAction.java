package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.SoldAllReservedInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action when reserved stock is confirmed sold (picked, shipped).
 * Confirms the reservation which deducts from total quantity.
 * Auto-state CHECK_DEPLETION routes to OUT_OF_STOCK or back to IN_WAREHOUSE.
 */
public class SoldAllReservedInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, SoldAllReservedInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(SoldAllReservedInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            SoldAllReservedInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        if (payload.getOrderId() != null) {
            inventory.confirmReservation(payload.getOrderId());
        }

        log.info("Reservation confirmed as sold for productId={}, orderId={}, remainingQty={}, available={}",
                inventory.getProductId(), payload.getOrderId(),
                inventory.getQuantity(), inventory.getAvailableQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
    }
}
