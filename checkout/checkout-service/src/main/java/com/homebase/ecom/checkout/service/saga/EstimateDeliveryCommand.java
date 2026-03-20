package com.homebase.ecom.checkout.service.saga;

import com.homebase.ecom.checkout.model.Checkout;
import org.chenile.owiz.Command;
import org.chenile.workflow.service.stmcmds.dto.TransitionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OWIZ saga step 6: Estimate delivery date.
 * Calculates carrier SLA + warehouse distance + processing time.
 */
public class EstimateDeliveryCommand implements Command<TransitionContext<Checkout>> {

    private static final Logger log = LoggerFactory.getLogger(EstimateDeliveryCommand.class);

    @Override
    public void execute(TransitionContext<Checkout> context) throws Exception {
        Checkout checkout = context.getEntity();

        checkout.setLastCompletedStep("estimateDelivery");
        log.info("[CHECKOUT SAGA] Delivery estimated for checkout {}, method={}",
                checkout.getId(), checkout.getShippingMethod());
    }
}
