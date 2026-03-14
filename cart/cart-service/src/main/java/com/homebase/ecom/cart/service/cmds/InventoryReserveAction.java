package com.homebase.ecom.cart.service.cmds;

import com.homebase.ecom.cart.dto.InventoryReservePayload;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.action.STMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.chenile.stm.model.Transition;

/**
 * Triggered by the Inventory Service to soft-block items for this cart.
 */
public class InventoryReserveAction extends AbstractCartAction<InventoryReservePayload> {

    @Autowired
    protected STMInternalTransitionInvoker<?> stmInternalTransitionInvoker;

    @Override
    public void transitionTo(Cart cart, InventoryReservePayload transitionParam, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stmInvoker, Transition transition) throws Exception {

        validateSystemAccess();

        InventoryReservePayload payload = transitionParam;

        if (payload != null && payload.isSuccess()) {
            logActivity(cart, "Inventory Reserved",
                    "Stock temporarily reserved. Reservation ID: " + payload.getReservationId());
        } else {
            logActivity(cart, "Inventory Reserve Check", "Proceeding with reserved cart state.");
        }
    }
}
