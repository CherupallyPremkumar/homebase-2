package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.OrderCreationPort;
import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 5: Create an order from checkout data.
 * Uses OrderCreationPort (hexagonal).
 */
public class CreateOrderCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderCommand.class);

    private final OrderCreationPort orderCreationPort;

    public CreateOrderCommand(OrderCreationPort orderCreationPort) {
        this.orderCreationPort = orderCreationPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        String orderId = orderCreationPort.createOrder(checkout);
        checkout.setOrderId(orderId);

        checkout.setLastCompletedStep("createOrder");
        log.info("[CHECKOUT SAGA] Order {} created for checkout {}",
                orderId, checkout.getId());
    }
}
