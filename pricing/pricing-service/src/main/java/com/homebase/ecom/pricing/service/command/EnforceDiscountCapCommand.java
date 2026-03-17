package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 6: Enforce maximum discount cap.
 * If total discount exceeds maxDiscountPercentage of subtotal, clamp it.
 * Also recalculates line totals.
 */
public class EnforceDiscountCapCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(EnforceDiscountCapCommand.class);
    private final PricingPolicyValidator policyValidator;

    public EnforceDiscountCapCommand(PricingPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("enforceDiscountCap")) return;

        int maxPercent = policyValidator.getMaxDiscountPercentage();
        String currency = ctx.getCurrency();

        // Calculate total line-level discounts
        Money totalLineDiscount = Money.zero(currency);
        for (LineItemPricing item : ctx.getLineItems()) {
            totalLineDiscount = totalLineDiscount.add(item.getLineDiscount());
            // Set line total = currentPrice * qty
            item.setLineTotal(item.getCurrentPrice().multiply(item.getQuantity()));
        }

        // Total discount = line-level + coupon/cart-level
        Money couponDiscount = ctx.getTotalDiscount().subtract(totalLineDiscount);
        if (couponDiscount.isNegative()) couponDiscount = Money.zero(currency);
        ctx.setTotalDiscount(totalLineDiscount.add(couponDiscount));

        // Enforce cap
        Money maxDiscount = Money.of(
                Math.round((double) ctx.getSubtotal().getAmount() * maxPercent / 100), currency);

        if (ctx.getTotalDiscount().isGreaterThan(maxDiscount)) {
            log.warn("Discount {} exceeds {}% cap ({}). Clamping.",
                    ctx.getTotalDiscount().toDisplayString(), maxPercent,
                    maxDiscount.toDisplayString());
            ctx.setTotalDiscount(maxDiscount);
        }

        log.debug("After cap enforcement: subtotal={}, discount={}, maxAllowed={}",
                ctx.getSubtotal().toDisplayString(),
                ctx.getTotalDiscount().toDisplayString(),
                maxDiscount.toDisplayString());
    }
}
