package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderStatus;
import com.homebase.ecom.order.dto.ProcessPaymentOrderPayload;

/**
 * STM Action: Handle payment failure for an order.
 * Transitions the order from CREATED to FAILED.
 * Increments retry count and stores error details for downstream handling.
 */
public class PaymentFailedOrderAction extends AbstractSTMTransitionAction<Order, ProcessPaymentOrderPayload> {

    private static final int MAX_RETRIES = 3;

    @Override
    public void transitionTo(Order order,
            ProcessPaymentOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Mark order as cancelled due to payment failure
        order.setStatus(OrderStatus.CANCELLED);

        // 2. Increment retry count
        int currentRetries = order.getRetryCount() != null ? order.getRetryCount() : 0;
        order.setRetryCount(currentRetries + 1);

        // 3. Store error details in transientMap
        if (payload != null && payload.getErrorDetails() != null) {
            order.getTransientMap().put("paymentErrorDetails", payload.getErrorDetails());
        }
        order.getTransientMap().put("retryCount", String.valueOf(order.getRetryCount()));

        // 4. If under max retries, track the failed order ID for retry
        if (order.getRetryCount() < MAX_RETRIES) {
            order.setPreviousFailedOrderId(order.getId());
            order.getTransientMap().put("retryEligible", "true");
        } else {
            order.getTransientMap().put("retryEligible", "false");
        }

        order.getTransientMap().previousPayload = payload;
    }
}
