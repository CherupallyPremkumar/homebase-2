package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PostSaveHook for CREATED state.
 * Logs order creation details. No Kafka event needed here because
 * PAYMENT_CONFIRMED hook handles downstream notification once payment succeeds.
 */
public class CREATEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(CREATEDOrderPostSaveHook.class);

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;
        String totalAmount = (order.getTotalAmount() != null) ? order.getTotalAmount().toString() : "N/A";

        log.info("Order created: orderId={}, items={}, totalAmount={}, customerId={}",
                order.getId(), itemCount, totalAmount, order.getUser_Id());
    }
}
