package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.RequestCancellationOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * STM Action: Request cancellation of an order.
 * Validates cancellation reason and sets cancellationAllowed flag for the
 * CHECK_CANCELLATION_WINDOW auto-state to evaluate.
 */
public class RequestCancellationOrderAction extends AbstractSTMTransitionAction<Order, RequestCancellationOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order, RequestCancellationOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // Validate cancellation reason
        policyValidator.validateCancellationReason(payload.getReason());

        // Set cancel reason on the order
        order.setCancelReason(payload.getReason());

        // Check cancellation window and set flag for auto-state
        boolean withinWindow = true;
        if (order.getCreatedTime() != null) {
            try {
                java.time.LocalDateTime createdAt = order.getCreatedTime().toInstant()
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime();
                policyValidator.validateCancellationWindow(createdAt);
            } catch (Exception e) {
                withinWindow = false;
            }
        }
        order.setCancellationAllowed(withinWindow);

        order.getTransientMap().previousPayload = payload;
    }
}
