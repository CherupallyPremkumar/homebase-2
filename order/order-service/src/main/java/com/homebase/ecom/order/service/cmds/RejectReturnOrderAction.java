package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.RejectReturnOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * STM Action: Reject a return request and revert the order to completed.
 * Transitions the order from RETURN_INITIATED to COMPLETED.
 * Enforces comment requirement policy and reverts item statuses.
 */
public class RejectReturnOrderAction extends AbstractSTMTransitionAction<Order, RejectReturnOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order,
            RejectReturnOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Check if comment is required on reject return
        if (policyValidator.isCommentRequiredOnRejectReturn()) {
            if (payload.getReason() == null || payload.getReason().trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "A reason is required when rejecting a return request per platform policy");
            }
        }

        // 2. Store rejection reason
        if (payload.getReason() != null) {
            order.getTransientMap().put("rejectionReason", payload.getReason());
        }
        if (payload.getRejectedBy() != null) {
            order.getTransientMap().put("rejectedBy", payload.getRejectedBy());
        }
        order.getTransientMap().put("rejectedAt", LocalDateTime.now().toString());

        // 3. Revert items from RETURN_REQUESTED back to PLACED (delivered status)
        order.getItems().stream()
                .filter(item -> item.getStatus() == OrderItemStatus.RETURN_REQUESTED)
                .forEach(item -> item.setStatus(OrderItemStatus.PLACED));

        order.getTransientMap().previousPayload = payload;
    }
}
