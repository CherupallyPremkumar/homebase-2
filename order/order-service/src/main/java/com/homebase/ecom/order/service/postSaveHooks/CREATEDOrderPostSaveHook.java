package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderCreatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * PostSaveHook for CREATED state.
 * Publishes ORDER_CREATED event to trigger downstream BCs (inventory reservation, etc.).
 */
public class CREATEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(CREATEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setCustomerId(order.getCustomerId());
        if (order.getTotalAmount() != null) {
            event.setTotalAmount(order.getTotalAmount());
        }
        event.setTimestamp(LocalDateTime.now());

        if (order.getItems() != null) {
            event.setItems(order.getItems().stream()
                    .map(item -> new OrderCreatedEvent.OrderItemPayload(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList()));
        }

        log.info("Publishing ORDER_CREATED event for order: {}, items: {}",
                order.getId(), order.getItems() != null ? order.getItems().size() : 0);
        orderEventPublisher.publishOrderCreated(event);
    }
}
