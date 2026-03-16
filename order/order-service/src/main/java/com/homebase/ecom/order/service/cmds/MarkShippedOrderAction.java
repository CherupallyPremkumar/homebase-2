package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.MarkShippedOrderPayload;

/**
 * STM Action: Mark order as shipped — transitions from PROCESSING to SHIPPED.
 * Only WAREHOUSE role can invoke.
 */
public class MarkShippedOrderAction extends AbstractSTMTransitionAction<Order, MarkShippedOrderPayload> {

    @Override
    public void transitionTo(Order order, MarkShippedOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null) {
            if (payload.getTrackingNumber() != null) {
                order.getTransientMap().put("trackingNumber", payload.getTrackingNumber());
            }
            if (payload.getCarrier() != null) {
                order.getTransientMap().put("carrier", payload.getCarrier());
            }
        }
        order.getTransientMap().previousPayload = payload;
    }
}
