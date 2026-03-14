package com.homebase.ecom.order.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;
import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.dto.DeliverOrderOrderPayload;

import java.time.LocalDateTime;

/**
 * STM Action: Mark order as delivered.
 * Transitions the order from SHIPPED to DELIVERED.
 * Records the delivery timestamp and stores delivery proof if provided.
 */
public class DeliverOrderOrderAction extends AbstractSTMTransitionAction<Order, DeliverOrderOrderPayload> {

    @Override
    public void transitionTo(Order order,
            DeliverOrderOrderPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        // 1. Set delivery date
        order.setDeliveryDate(LocalDateTime.now());

        // 2. Items in PLACED status are delivered by default (no status change needed)
        // Only non-cancelled items are considered delivered

        // 3. Store delivery proof in transientMap if provided
        if (payload.getDeliveryProof() != null) {
            order.getTransientMap().put("deliveryProof", payload.getDeliveryProof());
        }
        order.getTransientMap().put("deliveredAt", LocalDateTime.now().toString());

        order.getTransientMap().previousPayload = payload;
    }
}
