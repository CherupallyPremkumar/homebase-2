package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.MarkDeliveredShippingPayload;

import java.util.Date;

/**
 * Handles the markDelivered transition: OUT_FOR_DELIVERY -> DELIVERED.
 * Sets deliveredAt timestamp and captures delivery proof/signature.
 */
public class MarkDeliveredShippingAction extends AbstractSTMTransitionAction<Shipping,
        MarkDeliveredShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            MarkDeliveredShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Set delivered timestamp
        shipping.setDeliveredAt(new Date());

        // Capture delivery proof (photo, signature reference)
        String proof = payload.getDeliveryProof();
        if (proof != null && !proof.isEmpty()) {
            shipping.setDeliveryProof(proof);
        }

        // Update location to delivered destination
        shipping.setCurrentLocation("Delivered to " + (shipping.getShippingAddress() != null ? shipping.getShippingAddress() : "customer"));
    }
}
