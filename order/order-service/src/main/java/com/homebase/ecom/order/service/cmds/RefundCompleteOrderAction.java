package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.RefundCompleteOrderPayload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * STM Action: Complete the refund process for an order.
 * Transitions the order from REFUND_INITIATED to REFUNDED.
 * Marks refunded items and prepares settlement adjustment data.
 */
public class RefundCompleteOrderAction extends AbstractSTMTransitionAction<Order, RefundCompleteOrderPayload> {

    @Override
    public void transitionTo(Order order,
            RefundCompleteOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Determine which items to mark as refunded
        List<OrderItem> itemsToRefund;
        if (payload.getRefundedItemIds() != null && !payload.getRefundedItemIds().isEmpty()) {
            itemsToRefund = order.getItems().stream()
                    .filter(item -> payload.getRefundedItemIds().contains(item.getId()))
                    .collect(Collectors.toList());
        } else {
            // Refund all items that are in RETURN_REQUESTED or REFUND_REQUESTED status
            itemsToRefund = order.getItems().stream()
                    .filter(item -> item.getStatus() == OrderItemStatus.RETURN_REQUESTED
                            || item.getStatus() == OrderItemStatus.REFUND_REQUESTED)
                    .collect(Collectors.toList());
        }

        // 2. Mark items as REFUNDED
        for (OrderItem item : itemsToRefund) {
            item.refund();
        }

        // 3. Store refund transaction ID
        if (payload.getRefundTransactionId() != null) {
            order.getTransientMap().put("refundTransactionId", payload.getRefundTransactionId());
        }
        order.getTransientMap().put("refundCompletedAt", LocalDateTime.now().toString());
        order.getTransientMap().put("refundedItemCount", String.valueOf(itemsToRefund.size()));

        // 4. Prepare settlement adjustment data
        BigDecimal refundedTotal = itemsToRefund.stream()
                .map(item -> item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Build adjustment data grouped by supplier
        StringBuilder adjustmentData = new StringBuilder();
        itemsToRefund.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getSupplierId() != null ? item.getSupplierId() : "default"))
                .forEach((supplierId, items) -> {
                    BigDecimal supplierRefundTotal = items.stream()
                            .map(item -> item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (adjustmentData.length() > 0) {
                        adjustmentData.append(";");
                    }
                    adjustmentData.append(supplierId).append(":-").append(supplierRefundTotal.toPlainString());
                });

        order.getTransientMap().put("settlementAdjustment", adjustmentData.toString());
        order.getTransientMap().put("refundedTotal", refundedTotal.toPlainString());

        order.getTransientMap().previousPayload = payload;
    }
}
