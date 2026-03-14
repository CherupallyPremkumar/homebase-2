package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderDeliveredEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * PostSaveHook for DELIVERED state.
 * Publishes OrderDeliveredEvent after the order is delivered to the customer.
 */
public class DELIVEREDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(DELIVEREDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        LocalDateTime deliveredAt = (order.getDeliveryDate() != null)
                ? order.getDeliveryDate()
                : LocalDateTime.now();

        OrderDeliveredEvent event = new OrderDeliveredEvent(
                order.getId(),
                order.getUser_Id(),
                deliveredAt
        );

        log.info("Publishing OrderDeliveredEvent for order: {}, deliveredAt: {}", order.getId(), deliveredAt);
        orderEventPublisher.publishOrderDelivered(event);
    }
}
