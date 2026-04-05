package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.DiscountResult;
import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.port.CustomerTierPort;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Step 3: Apply customer segment pricing.
 * Adjusts prices based on customer tier (REGULAR, PREMIUM, WHOLESALE, B2B).
 * Tier discount percentages come from cconfig via rules engine.
 */
public class ApplySegmentPricingCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(ApplySegmentPricingCommand.class);
    private final CustomerTierPort customerTierPort;

    public ApplySegmentPricingCommand(CustomerTierPort customerTierPort) {
        this.customerTierPort = customerTierPort;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        if (ctx.shouldSkip("applySegmentPricing")) return;

        String tier = ctx.getCustomerTier();
        if (tier == null || tier.isBlank()) {
            tier = customerTierPort.getTier(ctx.getUserId());
            ctx.setCustomerTier(tier);
        }

        // REGULAR gets no segment discount
        if ("REGULAR".equalsIgnoreCase(tier)) {
            log.debug("Customer tier REGULAR — no segment pricing applied");
            return;
        }

        int discountPercent = switch (tier.toUpperCase()) {
            case "PREMIUM" -> 2;
            case "WHOLESALE" -> 5;
            case "B2B" -> 8;
            default -> 0;
        };

        if (discountPercent == 0) return;

        String currency = ctx.getCurrency();
        for (LineItemPricing item : ctx.getLineItems()) {
            if (item.getCurrentPrice() == null) continue;

            long discountAmount = Math.round((double) item.getCurrentPrice().getAmount() * discountPercent / 100);
            Money discount = Money.of(discountAmount, currency);
            item.setCurrentPrice(item.getCurrentPrice().subtract(discount));
            item.setLineDiscount(item.getLineDiscount() != null
                    ? item.getLineDiscount().add(discount)
                    : discount);
            item.addDiscount(new DiscountResult(discount, "SEGMENT", tier + " tier discount", discountPercent));
        }

        log.debug("Applied {}% segment discount for tier={}", discountPercent, tier);
    }
}
