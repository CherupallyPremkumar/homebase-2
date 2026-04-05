package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.RequestReturnOrderPayload;

/**
 * STM Action: Customer requests a return on a delivered/completed order.
 * Transitions to RETURN_REQUESTED.
 */
public class RequestReturnOrderAction extends AbstractSTMTransitionAction<Order, RequestReturnOrderPayload> {

    @Override
    public void transitionTo(Order order, RequestReturnOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getReason() != null) {
            order.getTransientMap().put("returnReason", payload.getReason());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
