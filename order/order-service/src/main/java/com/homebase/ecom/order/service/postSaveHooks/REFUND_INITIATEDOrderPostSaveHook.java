package com.homebase.ecom.order.service.postSaveHooks;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.RefundInitiatedEvent;
import org.chenile.stm.State;
import org.chenile.workflow.model.TransientMap;
import org.chenile.workflow.service.stmcmds.PostSaveHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PostSaveHook for REFUND_INITIATED state.
 * Publishes RefundInitiatedEvent to trigger payment BC to process the refund.
 */
public class REFUND_INITIATEDOrderPostSaveHook implements PostSaveHook<Order> {

    private static final Logger log = LoggerFactory.getLogger(REFUND_INITIATEDOrderPostSaveHook.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void execute(State startState, State endState, Order order, TransientMap map) {
        BigDecimal refundAmount = BigDecimal.ZERO;
        String currency = "USD";
        List<RefundInitiatedEvent.RefundItem> refundItems = new ArrayList<>();

        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                BigDecimal itemAmount = (item.getTotalPrice() != null)
                        ? item.getTotalPrice().getAmount()
                        : BigDecimal.ZERO;
                refundAmount = refundAmount.add(itemAmount);
                if (item.getTotalPrice() != null && item.getTotalPrice().getCurrency() != null) {
                    currency = item.getTotalPrice().getCurrency();
                }
                refundItems.add(new RefundInitiatedEvent.RefundItem(
                        item.getProductId(), item.getQuantity(), itemAmount));
            }
        }

        // Use total amount if available for accuracy
        if (order.getTotalAmount() != null) {
            refundAmount = order.getTotalAmount().getAmount();
            currency = order.getTotalAmount().getCurrency();
        }

        RefundInitiatedEvent event = new RefundInitiatedEvent(
                order.getId(),
                order.getUser_Id(),
                refundAmount,
                currency,
                LocalDateTime.now(),
                refundItems
        );

        log.info("Publishing RefundInitiatedEvent for order: {}, amount: {} {}",
                order.getId(), refundAmount, currency);
        orderEventPublisher.publishRefundInitiated(event);
    }
}
