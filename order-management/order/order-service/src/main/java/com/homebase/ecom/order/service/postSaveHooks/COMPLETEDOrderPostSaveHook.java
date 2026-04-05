package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for COMPLETED state.
 * Publishes ORDER_COMPLETED event to trigger settlement processing.
 */
public class COMPLETEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDOrderPostSaveHook.class);

    private final OrderEventPublisherPort eventPublisher;

    public COMPLETEDOrderPostSaveHook(OrderEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        log.info("Order {} entered COMPLETED state, publishing event", order.getId());
        eventPublisher.publishOrderCompleted(order);
    }
}
