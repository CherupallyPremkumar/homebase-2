package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderStatus;
import com.homebase.ecom.order.dto.ProcessPaymentOrderPayload;

import java.time.LocalDateTime;

/**
 * STM Action: Process a successful payment for an order.
 * Transitions the order from CREATED to PAYMENT_CONFIRMED.
 * Sets the gateway transaction ID and marks payment as processed.
 */
public class ProcessPaymentOrderAction extends AbstractSTMTransitionAction<Order, ProcessPaymentOrderPayload> {

    @Override
    public void transitionTo(Order order,
            ProcessPaymentOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Set gateway transaction ID from payment
        if (payload.getPaymentId() != null) {
            order.setGatewayTransactionId(payload.getPaymentId());
        }

        // 2. Mark payment as processed
        order.setStatus(OrderStatus.PAID);
        order.setWebhookProcessedAt(LocalDateTime.now());

        // 3. Store payment info in transientMap for PostSaveHook
        order.getTransientMap().put("paymentId", payload.getPaymentId());
        order.getTransientMap().put("paymentProcessedAt", LocalDateTime.now().toString());
        order.getTransientMap().previousPayload = payload;
    }
}
