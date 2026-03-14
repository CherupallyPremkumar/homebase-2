package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.ApproveReturnOrderPayload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * STM Action: Approve a return request and initiate the refund process.
 * Transitions the order from RETURN_INITIATED to REFUND_INITIATED.
 * Validates admin authority and calculates refund amount.
 */
public class ApproveReturnOrderAction extends AbstractSTMTransitionAction<Order, ApproveReturnOrderPayload> {

    @Override
    public void transitionTo(Order order,
            ApproveReturnOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Validate admin has authority (approvedBy must be provided)
        if (payload.getApprovedBy() == null || payload.getApprovedBy().trim().isEmpty()) {
            throw new IllegalArgumentException("Approver identity is required for return approval");
        }

        // 2. Determine which items are approved for return
        List<OrderItem> returnItems;
        if (payload.getApprovedItemIds() != null && !payload.getApprovedItemIds().isEmpty()) {
            returnItems = order.getItems().stream()
                    .filter(item -> payload.getApprovedItemIds().contains(item.getId()))
                    .collect(Collectors.toList());
        } else {
            // Approve all items that have RETURN_REQUESTED status
            returnItems = order.getItems().stream()
                    .filter(item -> item.getStatus() == OrderItemStatus.RETURN_REQUESTED)
                    .collect(Collectors.toList());
        }

        // 3. Calculate refund amount from the approved items
        BigDecimal refundAmount = returnItems.stream()
                .map(item -> item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Store refund amount and approved items in transientMap
        order.getTransientMap().put("refundAmount", refundAmount.toPlainString());
        order.getTransientMap().put("approvedBy", payload.getApprovedBy());
        order.getTransientMap().put("approvedItemCount", String.valueOf(returnItems.size()));
        order.getTransientMap().put("approvedAt", LocalDateTime.now().toString());

        String approvedItemIds = returnItems.stream()
                .map(OrderItem::getId)
                .collect(Collectors.joining(","));
        order.getTransientMap().put("approvedItemIds", approvedItemIds);

        order.getTransientMap().previousPayload = payload;
    }
}
