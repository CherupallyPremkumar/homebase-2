package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.PlaceOnHoldOrderPayload;

/**
 * STM Action: Place order on hold -- admin holds order for investigation.
 */
public class PlaceOnHoldOrderAction extends AbstractSTMTransitionAction<Order, PlaceOnHoldOrderPayload> {

    @Override
    public void transitionTo(Order order, PlaceOnHoldOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getHoldReason() != null) {
            order.setNotes((order.getNotes() != null ? order.getNotes() + "; " : "")
                    + "HOLD: " + payload.getHoldReason());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
