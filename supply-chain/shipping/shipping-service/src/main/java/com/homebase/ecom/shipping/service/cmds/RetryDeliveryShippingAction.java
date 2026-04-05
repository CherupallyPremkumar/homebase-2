package com.homebase.ecom.shipping.service.cmds;

import org.chenile.stm.STMInternalTransitionInvoker;
import org.chenile.stm.State;
import org.chenile.stm.model.Transition;

import org.chenile.workflow.service.stmcmds.AbstractSTMTransitionAction;
import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.dto.RetryDeliveryShippingPayload;
import com.homebase.ecom.shipping.service.validator.ShippingPolicyValidator;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Handles the retryDelivery transition: DELIVERY_FAILED -> CHECK_DELIVERY_ATTEMPTS.
 * Warehouse schedules another delivery attempt. Auto-state will check attempt count.
 */
public class RetryDeliveryShippingAction extends AbstractSTMTransitionAction<Shipping,
        RetryDeliveryShippingPayload> {

    @Autowired
    private ShippingPolicyValidator policyValidator;

    @Override
    public void transitionTo(Shipping shipping,
            RetryDeliveryShippingPayload payload,
            State startState, String eventId,
            State endState, STMInternalTransitionInvoker<?> stm, Transition transition) throws Exception {

        shipping.getTransientMap().previousPayload = payload;

        // Validate delivery attempt limits
        policyValidator.validateDeliveryAttemptLimit(shipping.getDeliveryAttempts());

        // Update delivery instructions if provided
        String instructions = payload.getNewDeliveryInstructions();
        if (instructions != null && !instructions.isEmpty()) {
            shipping.setDeliveryInstructions(instructions);
        }

        // Update location
        shipping.setCurrentLocation("Retry delivery scheduled - attempt " + (shipping.getDeliveryAttempts() + 1));
    }
}
