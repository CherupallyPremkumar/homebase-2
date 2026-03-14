package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.model.OrderItemStatus;
import com.homebase.ecom.order.dto.ItemsPickedOrderPayload;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * STM Action: Mark order items as picked from warehouse.
 * Transitions the order from PROCESSING to PICKED.
 * Validates that all active items are in PLACED status (not cancelled).
 */
public class ItemsPickedOrderAction extends AbstractSTMTransitionAction<Order, ItemsPickedOrderPayload> {

    @Override
    public void transitionTo(Order order,
            ItemsPickedOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Validate all non-cancelled items are in PLACED status
        List<OrderItem> activeItems = order.getItems().stream()
                .filter(item -> item.getStatus() != OrderItemStatus.CANCELLED
                        && item.getStatus() != OrderItemStatus.CANCELLATION_REQUESTED)
                .collect(Collectors.toList());

        if (activeItems.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot pick items: order " + order.getId() + " has no active items");
        }

        for (OrderItem item : activeItems) {
            if (item.getStatus() != OrderItemStatus.PLACED) {
                throw new IllegalStateException(
                        "Cannot pick items: item " + item.getId() + " is in status " + item.getStatus()
                                + ", expected PLACED");
            }
        }

        // 2. Store picked timestamp
        order.getTransientMap().put("pickedAt", LocalDateTime.now().toString());

        // 3. Prepare packing slip data in transientMap
        String packingSlipItems = activeItems.stream()
                .map(item -> item.getProductId() + ":" + item.getQuantity())
                .collect(Collectors.joining(","));
        order.getTransientMap().put("packingSlipItems", packingSlipItems);
        order.getTransientMap().put("packingSlipItemCount", String.valueOf(activeItems.size()));

        order.getTransientMap().previousPayload = payload;
    }
}
