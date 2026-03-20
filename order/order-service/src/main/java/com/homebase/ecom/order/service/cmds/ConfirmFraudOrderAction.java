package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.ConfirmFraudOrderPayload;

/**
 * STM Action: Confirm fraud -- admin confirms order is fraudulent.
 * Transitions from FRAUD_HOLD to CANCELLED.
 */
public class ConfirmFraudOrderAction extends AbstractSTMTransitionAction<Order, ConfirmFraudOrderPayload> {

    @Override
    public void transitionTo(Order order, ConfirmFraudOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        order.setCancelReason("Fraud confirmed: " +
                (payload != null && payload.getFraudType() != null ? payload.getFraudType() : "UNSPECIFIED"));
        order.getTransientMap().previousPayload = payload;
    }
}
