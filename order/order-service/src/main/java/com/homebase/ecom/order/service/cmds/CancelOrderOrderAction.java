package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.CancelOrderOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * STM Action: Cancel an order.
 * Enforces:
 * - policies.cancellation.requireCancellationReason (reason must be provided)
 * - policies.cancellation.cancellationWindowHours (must be within window)
 */
@Component
public class CancelOrderOrderAction extends AbstractSTMTransitionAction<Order, CancelOrderOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order,
            CancelOrderOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // --- Policy: cancellation reason required ---
        policyValidator.validateCancellationReason(payload.getReason());

        // --- Policy: cancellation window check ---
        if (order.getCreatedTime() != null) {
            java.time.LocalDateTime createdAt = order.getCreatedTime().toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDateTime();
            policyValidator.validateCancellationWindow(createdAt);
        }

        order.getTransientMap().previousPayload = payload;
    }
}
