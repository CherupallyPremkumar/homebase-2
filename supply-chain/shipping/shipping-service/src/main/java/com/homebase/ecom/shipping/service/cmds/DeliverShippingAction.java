package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.DeliverShippingPayload;

import java.util.Date;

/**
 * Handles the deliver transition: OUT_FOR_DELIVERY -> DELIVERED.
 * Sets delivered timestamp, captures delivery proof.
 */
public class DeliverShippingAction extends AbstractSTMTransitionAction<Shipping,
        DeliverShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            DeliverShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Set actual delivery date
        shipping.setActualDeliveryDate(new Date());

        // Increment delivery attempts
        shipping.setDeliveryAttempts(shipping.getDeliveryAttempts() + 1);

        // Update location to delivered destination
        shipping.setCurrentLocation("Delivered to " +
                (shipping.getToAddress() != null ? "customer address" : "customer"));
    }
}
