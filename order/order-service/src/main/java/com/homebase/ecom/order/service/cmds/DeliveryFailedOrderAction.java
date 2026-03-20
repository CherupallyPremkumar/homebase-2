package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.DeliveryFailedOrderPayload;

/**
 * STM Action: Delivery failed -- all carrier delivery attempts exhausted.
 * Transitions from SHIPPED to DELIVERY_FAILED.
 */
public class DeliveryFailedOrderAction extends AbstractSTMTransitionAction<Order, DeliveryFailedOrderPayload> {

    @Override
    public void transitionTo(Order order, DeliveryFailedOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getFailureReason() != null) {
            order.getTransientMap().put("deliveryFailureReason", payload.getFailureReason());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
