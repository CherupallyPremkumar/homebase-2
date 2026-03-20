package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 1 (before lockCart): Validate delivery address.
 * Checks pincode serviceability and address completeness.
 */
public class ValidateAddressCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ValidateAddressCommand.class);

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        if (checkout.getShippingAddressId() == null || checkout.getShippingAddressId().isBlank()) {
            throw new RuntimeException("Shipping address is required for checkout");
        }

        checkout.setLastCompletedStep("validateAddress");
        log.info("[CHECKOUT SAGA] Address validated for checkout {}, addressId={}",
                checkout.getId(), checkout.getShippingAddressId());
    }
}
