package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderCompletedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostSaveHook for COMPLETED state.
 * Publishes OrderCompletedEvent with items and amounts to trigger settlement processing.
 */
public class COMPLETEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(COMPLETEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        List<OrderCompletedEvent.CompletedItem> completedItems = new ArrayList<>();
        if (order.getItems() != null) {
            completedItems = order.getItems().stream()
                    .map(item -> new OrderCompletedEvent.CompletedItem(
                            item.getProductId(),
                            item.getSupplierId(),
                            item.getQuantity(),
                            item.getTotalPrice() != null ? item.getTotalPrice().getAmount() : null
                    ))
                    .collect(Collectors.toList());
        }

        OrderCompletedEvent event = new OrderCompletedEvent(
                order.getId(),
                order.getUser_Id(),
                LocalDateTime.now(),
                completedItems
        );

        log.info("Publishing OrderCompletedEvent for order: {}, items: {}", order.getId(), completedItems.size());
        orderEventPublisher.publishOrderCompleted(event);
    }
}
