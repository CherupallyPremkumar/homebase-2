package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.OrderRefundedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * PostSaveHook for REFUNDED state.
 * Publishes OrderRefundedEvent after refund is fully processed.
 */
public class REFUNDEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(REFUNDEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        String refundTransactionId = (map != null && map.get("refundTransactionId") != null)
                ? map.get("refundTransactionId").toString()
                : null;

        BigDecimal refundAmount = BigDecimal.ZERO;
        String currency = "USD";
        if (order.getTotalAmount() != null) {
            refundAmount = order.getTotalAmount().getAmount();
            currency = order.getTotalAmount().getCurrency();
        }

        OrderRefundedEvent event = new OrderRefundedEvent(
                order.getId(),
                order.getUser_Id(),
                refundTransactionId,
                refundAmount,
                currency,
                LocalDateTime.now()
        );

        log.info("Publishing OrderRefundedEvent for order: {}, transactionId: {}, amount: {} {}",
                order.getId(), refundTransactionId, refundAmount, currency);
        orderEventPublisher.publishOrderRefunded(event);
    }
}
