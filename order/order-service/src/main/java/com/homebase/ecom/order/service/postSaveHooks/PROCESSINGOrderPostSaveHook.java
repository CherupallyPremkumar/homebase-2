package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderProcessingStartedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * PostSaveHook for PROCESSING state.
 * Publishes OrderProcessingStartedEvent so the shipping BC can prepare.
 */
public class PROCESSINGOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(PROCESSINGOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String warehouseId = (map != null && map.get("warehouseId") != null)
                ? map.get("warehouseId").toString()
                : "DEFAULT_WAREHOUSE";

        OrderProcessingStartedEvent event = new OrderProcessingStartedEvent(
                order.getId(),
                warehouseId,
                LocalDateTime.now()
        );

        log.info("Publishing OrderProcessingStartedEvent for order: {}", order.getId());
        orderEventPublisher.publishOrderProcessingStarted(event);
    }
}
