package com.homebase.ecom.order.service.cmds;

import com.homebase.ecom.order.model.CancelOrderItemPayload;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderItemCancellationRequestedEvent;
import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Action to handle item-level cancellation request within an order.
 */
@Component
public class CancelOrderItemAction extends AbstractSTMTransitionAction<Order, CancelOrderItemPayload> {

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void transitionTo(Order order, CancelOrderItemPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        String itemId = payload.getOrderItemId();

        // 1. Update the Order Aggregate
        order.cancelItem(itemId);

        // 2. Publish Item Cancellation Requested Event
        OrderItem item = order.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("OrderItem not found: " + itemId));

        OrderItemCancellationRequestedEvent event = new OrderItemCancellationRequestedEvent(
                order.getId(),
                itemId,
                item.getProductId(),
                item.getQuantity(),
                payload.getReason());

        orderEventPublisher.publishOrderItemCancellationRequested(event);
    }
}
