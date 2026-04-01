package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.service.PricingPolicyValidator;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 2: Validate seller listing price against MRP rules.
 * Ensures seller price doesn't violate minimum price thresholds
 * (e.g., max discount percentage from MRP defined by marketplace policy).
 */
public class ValidateSellerMinPriceCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ValidateSellerMinPriceCommand.class);
    private final PricingPolicyValidator policyValidator;

    public ValidateSellerMinPriceCommand(PricingPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("validateSellerMinPrice")) return;

        int maxDiscountPercent = policyValidator.getMaxDiscountPercentage();

        for (LineItemPricing item : ctx.getLineItems()) {
            if (item.getUnitPrice() == null || item.getCurrentPrice() == null) continue;

            long mrp = item.getUnitPrice().getAmount();
            long sellerPrice = item.getCurrentPrice().getAmount();

            if (mrp > 0) {
                long minAllowed = Math.round((double) mrp * (100 - maxDiscountPercent) / 100);
                if (sellerPrice < minAllowed) {
                    log.warn("Seller price {} below minimum {} for variant={}, clamping to minimum",
                            sellerPrice, minAllowed, item.getVariantId());
                    item.setCurrentPrice(
                            com.homebase.ecom.shared.Money.of(minAllowed, ctx.getCurrency()));
                }
            }
        }

        log.debug("Seller min price validation complete for {} items", ctx.getLineItems().size());
    }
}
