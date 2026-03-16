package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderShippedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * PostSaveHook for SHIPPED state.
 * Publishes ORDER_SHIPPED event.
 */
public class SHIPPEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(SHIPPEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String carrier = (map != null && map.get("carrier") != null)
                ? map.get("carrier").toString() : "UNKNOWN";
        String trackingNumber = (map != null && map.get("trackingNumber") != null)
                ? map.get("trackingNumber").toString() : null;

        OrderShippedEvent event = new OrderShippedEvent(
                order.getId(), carrier, trackingNumber, null, LocalDateTime.now()
        );

        log.info("Publishing ORDER_SHIPPED event for order: {}, carrier: {}", order.getId(), carrier);
        orderEventPublisher.publishOrderShipped(event);
    }
}
