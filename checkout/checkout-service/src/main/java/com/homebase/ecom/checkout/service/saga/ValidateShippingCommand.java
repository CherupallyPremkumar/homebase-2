package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 4: Validate shipping feasibility.
 * Currently validates that shipping address is present.
 * Future: call shipping service to validate address + carrier availability.
 */
public class ValidateShippingCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ValidateShippingCommand.class);

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        if (checkout.getShippingAddressId() == null || checkout.getShippingAddressId().isBlank()) {
            throw new RuntimeException("Shipping address is required for checkout");
        }

        log.info("[CHECKOUT SAGA] Shipping validated for checkout {}, address={}, method={}",
                checkout.getId(), checkout.getShippingAddressId(), checkout.getShippingMethod());

        checkout.setLastCompletedStep("validateShipping");
    }
}
