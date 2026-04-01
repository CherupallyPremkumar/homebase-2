package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.RequestRefundOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM Action: Request refund — transitions from DELIVERED/COMPLETED to REFUND_REQUESTED.
 */
public class RequestRefundOrderAction extends AbstractSTMTransitionAction<Order, RequestRefundOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order, RequestRefundOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        if (payload != null && payload.getReason() != null) {
            policyValidator.validateReturnReason(payload.getReason());
        }
        order.getTransientMap().previousPayload = payload;
    }
}
