package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.ConfirmDeliveryOrderPayload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * STM Action: Confirm delivery and complete the order.
 * Transitions the order from DELIVERED to COMPLETED.
 * Prepares settlement trigger data for supplier payouts.
 */
public class ConfirmDeliveryOrderAction extends AbstractSTMTransitionAction<Order, ConfirmDeliveryOrderPayload> {

    @Override
    public void transitionTo(Order order,
            ConfirmDeliveryOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Store completion timestamp
        order.getTransientMap().put("completedAt", LocalDateTime.now().toString());

        // 2. Store who confirmed
        if (payload.getConfirmedBy() != null) {
            order.getTransientMap().put("confirmedBy", payload.getConfirmedBy());
        }

        // 3. Prepare settlement trigger data (supplierId -> items and amounts)
        List<OrderItem> deliveredItems = order.getItems().stream()
                .filter(item -> item.getStatus() == OrderItemStatus.PLACED)
                .collect(Collectors.toList());

        // Build settlement summary: supplierId:totalAmount pairs
        StringBuilder settlementData = new StringBuilder();
        deliveredItems.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getSupplierId() != null ? item.getSupplierId() : "default"))
                .forEach((supplierId, items) -> {
                    BigDecimal supplierTotal = items.stream()
                            .map(item -> item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    if (settlementData.length() > 0) {
                        settlementData.append(";");
                    }
                    settlementData.append(supplierId).append(":").append(supplierTotal.toPlainString());
                });

        order.getTransientMap().put("settlementData", settlementData.toString());
        order.getTransientMap().put("settlementItemCount", String.valueOf(deliveredItems.size()));

        order.getTransientMap().previousPayload = payload;
    }
}
