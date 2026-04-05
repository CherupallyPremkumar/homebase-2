package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for SHIPPED state.
 * Publishes ORDER_SHIPPED event with carrier and tracking information.
 */
public class SHIPPEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(SHIPPEDOrderPostSaveHook.class);

    private final OrderEventPublisherPort eventPublisher;

    public SHIPPEDOrderPostSaveHook(OrderEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String carrier = (map != null && map.get("carrier") != null)
                ? map.get("carrier").toString() : "UNKNOWN";
        String trackingNumber = (map != null && map.get("trackingNumber") != null)
                ? map.get("trackingNumber").toString() : null;

        log.info("Order {} entered SHIPPED state, carrier: {}", order.getId(), carrier);
        eventPublisher.publishOrderShipped(order, carrier, trackingNumber);
    }
}
