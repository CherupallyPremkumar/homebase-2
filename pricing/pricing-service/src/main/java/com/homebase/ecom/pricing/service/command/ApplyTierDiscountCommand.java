package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.DiscountResult;
import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 3: Apply customer tier discount per line item.
 * Reads tier→percentage mapping from cconfig.
 * Multiplicative: applied on currentPrice after volume discount.
 */
public class ApplyTierDiscountCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplyTierDiscountCommand.class);
    private final PricingPolicyValidator policyValidator;

    public ApplyTierDiscountCommand(PricingPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        String tier = ctx.getCustomerTier();
        if (tier == null || tier.isBlank()) return;

        int percent = policyValidator.getCustomerTierDiscount(tier);
        if (percent <= 0) return;

        for (LineItemPricing item : ctx.getLineItems()) {
            Money discountPerUnit = Money.of(
                    Math.round((double) item.getCurrentPrice().getAmount() * percent / 100),
                    ctx.getCurrency());
            Money newPrice = item.getCurrentPrice().subtract(discountPerUnit);
            item.setCurrentPrice(newPrice);

            Money totalDiscount = discountPerUnit.multiply(item.getQuantity());
            item.setLineDiscount(item.getLineDiscount().add(totalDiscount));
            item.addDiscount(new DiscountResult(totalDiscount, "TIER",
                    "Customer tier " + tier + ": " + percent + "% off", percent));

            log.debug("Tier discount {}% ({}) on variant {}: -{}",
                    percent, tier, item.getVariantId(), totalDiscount.toDisplayString());
        }
    }
}
