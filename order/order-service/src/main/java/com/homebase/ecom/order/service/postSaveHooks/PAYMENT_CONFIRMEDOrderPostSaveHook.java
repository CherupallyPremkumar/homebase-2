package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderCreatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Contains customized post Save Hook for the State ID.
 */
public class PAYMENT_CONFIRMEDOrderPostSaveHook implements PostSaveHook<Order> {

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(order.getId());
        event.setCustomerId(order.getUser_Id());
        if (order.getTotalAmount() != null) {
            event.setTotalAmount(order.getTotalAmount().getAmount());
        }
        event.setTimestamp(LocalDateTime.now());

        if (order.getItems() != null) {
            event.setItems(order.getItems().stream()
                    .map(item -> new OrderCreatedEvent.OrderItemPayload(item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList()));
        }

        orderEventPublisher.publishOrderCreated(event);
    }
}
