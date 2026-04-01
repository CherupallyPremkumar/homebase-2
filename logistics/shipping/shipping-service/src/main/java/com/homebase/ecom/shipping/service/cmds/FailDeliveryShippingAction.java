package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.FailDeliveryShippingPayload;

/**
 * Handles the failDelivery transition: OUT_FOR_DELIVERY -> DELIVERY_FAILED.
 * Records delivery failure and increments attempt counter.
 */
public class FailDeliveryShippingAction extends AbstractSTMTransitionAction<Shipping,
        FailDeliveryShippingPayload> {

    @Override
    public void transitionTo(Shipping shipping,
            FailDeliveryShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Increment delivery attempts
        shipping.setDeliveryAttempts(shipping.getDeliveryAttempts() + 1);

        // Record failure reason in delivery instructions
        String reason = payload.getFailureReason();
        if (reason != null && !reason.isEmpty()) {
            shipping.setDeliveryInstructions(
                    "Attempt " + shipping.getDeliveryAttempts() + " failed: " + reason);
        }

        // Update location
        shipping.setCurrentLocation("Delivery failed - returned to local hub");
    }
}
