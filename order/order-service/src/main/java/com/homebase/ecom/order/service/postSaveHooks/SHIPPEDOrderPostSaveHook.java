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
 * Publishes OrderShippedEvent to trigger shipping BC to create a shipment record.
 * Reads carrier and tracking info from the transientMap (set by CourierPickupOrderAction).
 */
public class SHIPPEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(SHIPPEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String carrier = (map != null && map.get("carrier") != null)
                ? map.get("carrier").toString()
                : "UNKNOWN";
        String trackingNumber = (map != null && map.get("trackingNumber") != null)
                ? map.get("trackingNumber").toString()
                : null;

        LocalDateTime estimatedDelivery = order.getDeliveryDate();

        OrderShippedEvent event = new OrderShippedEvent(
                order.getId(),
                carrier,
                trackingNumber,
                estimatedDelivery,
                LocalDateTime.now()
        );

        log.info("Publishing OrderShippedEvent for order: {}, carrier: {}, tracking: {}",
                order.getId(), carrier, trackingNumber);
        orderEventPublisher.publishOrderShipped(event);
    }
}
