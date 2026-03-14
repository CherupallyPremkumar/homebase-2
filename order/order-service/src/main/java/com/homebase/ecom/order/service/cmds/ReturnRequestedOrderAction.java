package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.ReturnRequestedOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * STM Action: Handle a return request for a shipped order.
 * Transitions the order from SHIPPED to RETURN_INITIATED.
 * Validates the return reason using OrderPolicyValidator.
 */
public class ReturnRequestedOrderAction extends AbstractSTMTransitionAction<Order, ReturnRequestedOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order,
            ReturnRequestedOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Validate the return reason against allowed reasons
        policyValidator.validateReturnReason(payload.getReason());

        // 2. Store return request details
        if (payload.getReason() != null) {
            order.getTransientMap().put("returnReason", payload.getReason());
        }
        order.getTransientMap().put("returnRequestedAt", LocalDateTime.now().toString());

        // 3. Mark relevant items as RETURN_REQUESTED
        List<String> itemIds = payload.getItemIds();
        if (itemIds != null && !itemIds.isEmpty()) {
            for (String itemId : itemIds) {
                OrderItem item = order.getItems().stream()
                        .filter(i -> i.getId().equals(itemId))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + itemId));
                item.setStatus(OrderItemStatus.RETURN_REQUESTED);
            }
        } else {
            // If no specific items, mark all PLACED items as return requested
            order.getItems().stream()
                    .filter(item -> item.getStatus() == OrderItemStatus.PLACED)
                    .forEach(item -> item.setStatus(OrderItemStatus.RETURN_REQUESTED));
        }

        order.getTransientMap().previousPayload = payload;
    }
}
