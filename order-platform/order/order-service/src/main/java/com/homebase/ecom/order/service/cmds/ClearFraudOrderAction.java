package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.ClearFraudOrderPayload;

/**
 * STM Action: Clear fraud hold -- admin reviewed and approved order.
 * Transitions from FRAUD_HOLD to PROCESSING.
 */
public class ClearFraudOrderAction extends AbstractSTMTransitionAction<Order, ClearFraudOrderPayload> {

    @Override
    public void transitionTo(Order order, ClearFraudOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getClearanceNote() != null) {
            order.getTransientMap().put("clearanceNote", payload.getClearanceNote());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
