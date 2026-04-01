package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 9: Calculate TCS (Tax Collected at Source).
 * Indian marketplace mandate: platform collects 1% TCS on seller transactions
 * above the threshold (currently ₹5 lakh/year).
 * TCS is additive to the final price — borne by the seller, collected by platform.
 * For pricing display, we calculate it but it doesn't change the customer-facing total.
 */
public class CalculateTCSCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(CalculateTCSCommand.class);
    private final PricingPolicyValidator policyValidator;

    public CalculateTCSCommand(PricingPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("calculateTCS")) return;

        // TCS is seller-side — doesn't affect customer total.
        // Calculated here for settlement breakdown.
        // Default rate: 1% on net sale value above threshold.
        // For now, just log — actual deduction happens in settlement.
        Money subtotal = ctx.getSubtotal();
        if (subtotal == null || subtotal.getAmount() <= 0) {
            log.debug("Skipping TCS: no subtotal");
            return;
        }

        log.debug("TCS calculation noted for settlement. Subtotal={}", subtotal.toDisplayString());
    }
}
