package com.homebase.ecom.cart.service.cmds;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.cart.model.Cart;
import org.chenile.workflow.param.MinimalPayload;
import org.springframework.beans.factory.annotation.Autowired;
import com.homebase.ecom.cart.service.event.CartEventPublisher;
import com.homebase.ecom.shared.model.event.OrderCreatedEvent;

public class OrderCreatedFromCartAction extends AbstractSTMTransitionAction<Cart, MinimalPayload> {
    @Autowired
    private CartEventPublisher cartEventPublisher;

    @Override
    public void transitionTo(Cart cart, MinimalPayload payload, State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setOrderId(cart.getId());
        event.setCustomerId(cart.getUserId());
        event.setTimestamp(LocalDateTime.now());

        event.setItems(cart.getItems().stream()
                .map(item -> new OrderCreatedEvent.OrderItemPayload(item.getProductId(), item.getQuantity()))
                .collect(Collectors.toList()));

        // For now, total amount is set to ZERO as Cart doesn't store price yet.
        // This should be calculated from Offers in a real scenario.
        event.setTotalAmount(BigDecimal.ZERO);

        cartEventPublisher.publishOrderCreated(event);
    }
}
