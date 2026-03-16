package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.StartProcessingOrderPayload;
import com.homebase.ecom.order.service.validator.OrderPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

/**
 * STM Action: Start processing — transitions from PAID to PROCESSING.
 * Validates order has items before processing can begin.
 */
public class StartProcessingOrderAction extends AbstractSTMTransitionAction<Order, StartProcessingOrderPayload> {

    @Autowired
    private OrderPolicyValidator policyValidator;

    @Override
    public void transitionTo(Order order, StartProcessingOrderPayload payload,
                             State startState, String eventId, State endState,
                             STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {
        // Validate order has items
        policyValidator.validateOrderHasItems(order);

        if (payload != null && payload.getWarehouseId() != null) {
            order.getTransientMap().put("warehouseId", payload.getWarehouseId());
        }
        order.getTransientMap().put("processingStartedAt", LocalDateTime.now().toString());
        order.getTransientMap().previousPayload = payload;
    }
}
