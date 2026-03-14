package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderStatus;
import com.homebase.ecom.order.dto.StartProcessingOrderPayload;

import java.time.LocalDateTime;

/**
 * STM Action: Start processing an order after payment confirmation.
 * Transitions the order from PAYMENT_CONFIRMED to PROCESSING.
 * Validates that the order has items before processing can begin.
 */
public class StartProcessingOrderAction extends AbstractSTMTransitionAction<Order, StartProcessingOrderPayload> {

    @Override
    public void transitionTo(Order order,
            StartProcessingOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Validate order has items
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot start processing: order " + order.getId() + " has no items");
        }

        // 2. Set status
        order.setStatus(OrderStatus.PROCESSING);

        // 3. Store warehouse assignment info if available
        if (payload.getWarehouseId() != null) {
            order.getTransientMap().put("warehouseId", payload.getWarehouseId());
        }

        // 4. Store processing start timestamp
        order.getTransientMap().put("processingStartedAt", LocalDateTime.now().toString());
        order.getTransientMap().previousPayload = payload;
    }
}
