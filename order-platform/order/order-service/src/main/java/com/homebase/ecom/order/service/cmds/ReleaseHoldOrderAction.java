package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.ReleaseHoldOrderPayload;

/**
 * STM Action: Release hold -- admin releases order back to PROCESSING.
 */
public class ReleaseHoldOrderAction extends AbstractSTMTransitionAction<Order, ReleaseHoldOrderPayload> {

    @Override
    public void transitionTo(Order order, ReleaseHoldOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getReleaseNote() != null) {
            order.getTransientMap().put("releaseNote", payload.getReleaseNote());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
