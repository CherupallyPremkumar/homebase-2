package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.port.OrderEventPublisherPort;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for CANCELLED state.
 * Publishes ORDER_CANCELLED event to trigger inventory release and payment refund.
 */
public class CANCELLEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(CANCELLEDOrderPostSaveHook.class);

    private final OrderEventPublisherPort eventPublisher;

    public CANCELLEDOrderPostSaveHook(OrderEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        log.info("Order {} entered CANCELLED state, reason: {}", order.getId(), order.getCancelReason());
        eventPublisher.publishOrderCancelled(order);
    }
}
