package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.ReturnInitiatedEvent;
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
 * PostSaveHook for RETURN_INITIATED state.
 * Publishes ReturnInitiatedEvent with the items being returned and the reason.
 */
public class RETURN_INITIATEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(RETURN_INITIATEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String reason = (map != null && map.get("reason") != null)
                ? map.get("reason").toString()
                : "CUSTOMER_RETURN";

        List<ReturnInitiatedEvent.ReturnedItem> returnedItems = new ArrayList<>();
        if (order.getItems() != null) {
            returnedItems = order.getItems().stream()
                    .map(item -> new ReturnInitiatedEvent.ReturnedItem(
                            item.getProductId(), item.getQuantity()))
                    .collect(Collectors.toList());
        }

        ReturnInitiatedEvent event = new ReturnInitiatedEvent(
                order.getId(),
                order.getUser_Id(),
                reason,
                LocalDateTime.now(),
                returnedItems
        );

        log.info("Publishing ReturnInitiatedEvent for order: {}, reason: {}, items: {}",
                order.getId(), reason, returnedItems.size());
        orderEventPublisher.publishReturnInitiated(event);
    }
}
