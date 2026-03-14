package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.SettlementRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exit-action for COMPLETED state.
 * Publishes SettlementRequestedEvent to the settlement BC with order items,
 * amounts, and supplier IDs so that supplier payouts can be initiated.
 */
public class TriggerSettlementAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(TriggerSettlementAction.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        List<SettlementRequestedEvent.SettlementLineItem> lineItems = new ArrayList<>();
        if (order.getItems() != null) {
            lineItems = order.getItems().stream()
                    .map(item -> new SettlementRequestedEvent.SettlementLineItem(
                            item.getProductId(),
                            item.getSupplierId(),
                            item.getQuantity(),
                            item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : null
                    ))
                    .collect(Collectors.toList());
        }

        SettlementRequestedEvent event = new SettlementRequestedEvent(
                order.getId(),
                LocalDateTime.now(),
                lineItems
        );

        log.info("Triggering settlement for completed order: {}, lineItems: {}", order.getId(), lineItems.size());
        orderEventPublisher.publishSettlementRequested(event);
    }
}
