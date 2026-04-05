package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.MarkDeliveredOrderPayload;

/**
 * STM Action: Mark order as delivered — transitions from SHIPPED to DELIVERED.
 * Triggered by SYSTEM (shipping BC event) or manual.
 */
public class MarkDeliveredOrderAction extends AbstractSTMTransitionAction<Order, MarkDeliveredOrderPayload> {

    @Override
    public void transitionTo(Order order, MarkDeliveredOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        order.getTransientMap().previousPayload = payload;
    }
}
