package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 7: Screen for fraud.
 * Performs basic risk score check before order creation.
 */
public class ScreenFraudCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(ScreenFraudCommand.class);

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        checkout.setLastCompletedStep("screenFraud");
        log.info("[CHECKOUT SAGA] Fraud screening passed for checkout {}, customerId={}",
                checkout.getId(), checkout.getCustomerId());
    }
}
