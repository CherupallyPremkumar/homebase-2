package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.PartialShipOrderPayload;

/**
 * STM Action: Partial ship -- some items shipped, others still processing.
 */
public class PartialShipOrderAction extends AbstractSTMTransitionAction<Order, PartialShipOrderPayload> {

    @Override
    public void transitionTo(Order order, PartialShipOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null) {
            if (payload.getTrackingNumber() != null) {
                order.getTransientMap().put("partialTrackingNumber", payload.getTrackingNumber());
            }
            if (payload.getCarrier() != null) {
                order.getTransientMap().put("partialCarrier", payload.getCarrier());
            }
        }
        order.getTransientMap().previousPayload = payload;
    }
}
