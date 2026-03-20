package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.HoldForFraudOrderPayload;

/**
 * STM Action: Hold order for fraud review.
 * Stores risk score in transient map for CHECK_FRAUD auto-state evaluation.
 */
public class HoldForFraudOrderAction extends AbstractSTMTransitionAction<Order, HoldForFraudOrderPayload> {

    @Override
    public void transitionTo(Order order, HoldForFraudOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null) {
            if (payload.getRiskScore() != null) {
                order.getTransientMap().put("riskScore", payload.getRiskScore());
            }
            if (payload.getRiskDetails() != null) {
                order.getTransientMap().put("riskDetails", payload.getRiskDetails());
            }
        }
        order.getTransientMap().previousPayload = payload;
    }
}
