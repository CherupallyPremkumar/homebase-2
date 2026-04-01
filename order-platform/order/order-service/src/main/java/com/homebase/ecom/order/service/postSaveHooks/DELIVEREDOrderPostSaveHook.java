package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for DELIVERED state.
 * Publishes ORDER_DELIVERED event -- notifies customer.
 */
public class DELIVEREDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(DELIVEREDOrderPostSaveHook.class);

    private final OrderEventPublisherPort eventPublisher;

    public DELIVEREDOrderPostSaveHook(OrderEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        log.info("Order {} entered DELIVERED state, publishing event", order.getId());
        eventPublisher.publishOrderDelivered(order);
    }
}
