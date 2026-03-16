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
 * Publishes ORDER_DELIVERED event — notifies customer.
 */
public class DELIVEREDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(DELIVEREDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        OrderDeliveredEvent event = new OrderDeliveredEvent(
                order.getId(),
                order.getCustomerId(),
                LocalDateTime.now()
        );

        log.info("Publishing ORDER_DELIVERED event for order: {}", order.getId());
        orderEventPublisher.publishOrderDelivered(event);
    }
}
