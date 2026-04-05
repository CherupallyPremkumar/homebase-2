package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.domain.port.ShippingValidationPort;
import com.homebase.ecom.checkout.domain.port.ShippingValidationPort.ShippingResult;
import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 4: Validate shipping address and calculate shipping cost.
 * Uses ShippingValidationPort (hexagonal).
 */
public class ValidateShippingCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ValidateShippingCommand.class);

    private final ShippingValidationPort shippingValidationPort;

    public ValidateShippingCommand(ShippingValidationPort shippingValidationPort) {
        this.shippingValidationPort = shippingValidationPort;
    }

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        if (checkout.getShippingAddressId() == null || checkout.getShippingAddressId().isBlank()) {
            throw new RuntimeException("Shipping address is required for checkout");
        }

        String currency = checkout.getSubtotal() != null ? checkout.getSubtotal().getCurrency() : "INR";

        ShippingResult result = shippingValidationPort.validate(
                checkout.getShippingAddressId(),
                checkout.getShippingMethod(),
                currency
        );

        if (!result.valid()) {
            throw new RuntimeException("Shipping validation failed: " + result.reason());
        }

        checkout.setShippingCost(Money.of(result.shippingCost(), currency));

        checkout.setLastCompletedStep("validateShipping");
        log.info("[CHECKOUT SAGA] Shipping validated for checkout {}, cost={}, delivery={}",
                checkout.getId(), checkout.getShippingCost(), result.estimatedDelivery());
    }
}
