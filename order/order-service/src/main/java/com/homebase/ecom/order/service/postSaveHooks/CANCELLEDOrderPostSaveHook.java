package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.dto.CancelOrderOrderPayload;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderCancelledEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostSaveHook for CANCELLED state.
 * Publishes OrderCancelledEvent to trigger inventory release and payment refund.
 */
public class CANCELLEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(CANCELLEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        List<OrderCancelledEvent.OrderItemPayload> items = new ArrayList<>();
        if (order.getItems() != null) {
            items = order.getItems().stream()
                    .map(item -> new OrderCancelledEvent.OrderItemPayload(
                            item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());
        }

        OrderCancelledEvent event = new OrderCancelledEvent(
                order.getId(),
                order.getUser_Id(),
                items,
                LocalDateTime.now()
        );

        log.info("Publishing OrderCancelledEvent for order: {}, reason: {}",
                order.getId(), extractCancellationReason(map));
        orderEventPublisher.publishOrderCancelled(event);
    }

    private String extractCancellationReason(TransientMap map) {
        if (map != null && map.previousPayload != null
                && map.previousPayload instanceof CancelOrderOrderPayload) {
            return ((CancelOrderOrderPayload) map.previousPayload).getReason();
        }
        return "UNKNOWN";
    }
}
