package com.homebase.ecom.inventory.service.cmds;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.inventory.dto.ReleaseReservedStockInventoryPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * STM action for releasing reserved stock back to available inventory.
 * Finds the reservation by orderId and releases the quantity back.
 */
public class ReleaseReservedStockInventoryItemAction extends AbstractSTMTransitionAction<InventoryItem, ReleaseReservedStockInventoryPayload> {

    private static final Logger log = LoggerFactory.getLogger(ReleaseReservedStockInventoryItemAction.class);

    @Override
    public void transitionTo(InventoryItem inventory,
            ReleaseReservedStockInventoryPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        int prevReserved = inventory.getReservedQuantity();

        // Use domain method to release reservation
        inventory.releaseReservation(payload.getOrderId());

        int released = prevReserved - inventory.getReservedQuantity();

        // Update status if stock is no longer low
        if (inventory.getAvailableQuantity() > inventory.getLowStockThreshold()) {
            inventory.setStatus(InventoryStatus.AVAILABLE);
        }

        log.info("Released reservation for productId={}, orderId={}, releasedQty={}, newAvailable={}",
                inventory.getProductId(), payload.getOrderId(), released, inventory.getAvailableQuantity());

        inventory.getTransientMap().put("previousPayload", payload);
        inventory.getTransientMap().put("releasedQuantity", released);
    }
}
