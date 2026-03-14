package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.stm.action.STMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.service.event.OrderEventPublisher;
import com.homebase.ecom.shared.event.SettlementAdjustmentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Exit-action for REFUNDED state.
 * Publishes SettlementAdjustmentEvent to the settlement BC so that
 * supplier payout records can be adjusted after a refund.
 */
public class AdjustSettlementAction implements STMTransitionAction<Order> {

    private static final Logger log = LoggerFactory.getLogger(AdjustSettlementAction.class);

    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Override
    public void doTransition(Order order, Object transitionParam,
            State startState, String eventId, State endState,
            STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        BigDecimal adjustmentAmount = BigDecimal.ZERO;
        String currency = "USD";

        if (order.getTotalAmount() != null) {
            adjustmentAmount = order.getTotalAmount().getAmount();
            currency = order.getTotalAmount().getCurrency();
        }

        SettlementAdjustmentEvent event = new SettlementAdjustmentEvent(
                order.getId(),
                adjustmentAmount,
                currency,
                "REFUND",
                LocalDateTime.now()
        );

        log.info("Publishing settlement adjustment for refunded order: {}, amount: {} {}",
                order.getId(), adjustmentAmount, currency);
        orderEventPublisher.publishSettlementAdjustment(event);
    }
}
