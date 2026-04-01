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
 * Step 2: Apply volume-based quantity discounts per line item.
 * Reads tier thresholds from cconfig via PricingPolicyValidator.
 * Multiplicative stacking: discount applied on currentPrice (not original).
 */
public class ApplyVolumeDiscountCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplyVolumeDiscountCommand.class);
    private final PricingPolicyValidator policyValidator;

    public ApplyVolumeDiscountCommand(PricingPolicyValidator policyValidator) {
        this.policyValidator = policyValidator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        for (LineItemPricing item : ctx.getLineItems()) {
            int percent = policyValidator.getVolumeDiscountPercent(item.getQuantity());
            if (percent <= 0) continue;

            Money discountPerUnit = Money.of(
                    Math.round((double) item.getCurrentPrice().getAmount() * percent / 100),
                    ctx.getCurrency());
            Money newPrice = item.getCurrentPrice().subtract(discountPerUnit);
            item.setCurrentPrice(newPrice);

            Money totalDiscount = discountPerUnit.multiply(item.getQuantity());
            item.setLineDiscount(item.getLineDiscount().add(totalDiscount));
            item.addDiscount(new DiscountResult(totalDiscount, "VOLUME",
                    "Volume discount: " + percent + "% off for qty " + item.getQuantity(), percent));

            log.debug("Volume discount {}% on variant {} (qty {}): -{}",
                    percent, item.getVariantId(), item.getQuantity(), totalDiscount.toDisplayString());
        }
    }
}
