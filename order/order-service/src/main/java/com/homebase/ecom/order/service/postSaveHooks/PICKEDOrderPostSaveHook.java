package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderItemsPickedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * PostSaveHook for PICKED state.
 * Publishes OrderItemsPickedEvent after items are picked from the warehouse.
 */
public class PICKEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(PICKEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;

        OrderItemsPickedEvent event = new OrderItemsPickedEvent(
                order.getId(),
                itemCount,
                LocalDateTime.now()
        );

        log.info("Publishing OrderItemsPickedEvent for order: {}, itemCount: {}", order.getId(), itemCount);
        orderEventPublisher.publishOrderItemsPicked(event);
    }
}
