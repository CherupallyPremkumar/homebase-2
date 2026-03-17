package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.LineItemPricing;
import com.homebase.ecom.pricing.domain.model.PriceBreakdown;
import com.homebase.ecom.pricing.domain.model.PricingContext;
import com.homebase.ecom.pricing.domain.model.LineItemDiscount;
import com.homebase.ecom.pricing.domain.service.IHashCalculator;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Step 8: Assemble the immutable PriceBreakdown from context data.
 * This is the final step — reads all intermediate values and builds the result.
 * Computes breakdownHash via IHashCalculator for price-lock integrity verification.
 */
public class BuildPricingResponseCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(BuildPricingResponseCommand.class);
    private final IHashCalculator hashCalculator;

    public BuildPricingResponseCommand(IHashCalculator hashCalculator) {
        this.hashCalculator = hashCalculator;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        Money finalTotal = ctx.getSubtotal()
                .subtract(ctx.getTotalDiscount())
                .add(ctx.getTaxAmount())
                .add(ctx.getShippingCost());

        if (finalTotal.isNegative()) {
            finalTotal = Money.zero(ctx.getCurrency());
        }

        // Build line item discounts for PriceBreakdown
        List<LineItemDiscount> itemDiscounts = new ArrayList<>();
        for (LineItemPricing item : ctx.getLineItems()) {
            if (item.getLineDiscount().isPositive()) {
                LineItemDiscount lid = new LineItemDiscount();
                lid.setProductName(item.getVariantId());
                lid.setDiscountAmount(item.getLineDiscount());
                lid.setDiscountReason(buildDiscountReason(item));
                itemDiscounts.add(lid);
            }
        }

        // Compute integrity hash from price components
        String breakdownHash = hashCalculator.calculateBreakdownHash(
                String.valueOf(ctx.getSubtotal().getAmount()),
                String.valueOf(ctx.getTotalDiscount().getAmount()),
                String.valueOf(ctx.getTaxAmount().getAmount()),
                String.valueOf(ctx.getShippingCost().getAmount()),
                String.valueOf(finalTotal.getAmount()));

        PriceBreakdown breakdown = PriceBreakdown.builder()
                .subtotal(ctx.getSubtotal())
                .totalDiscount(ctx.getTotalDiscount())
                .taxAmount(ctx.getTaxAmount())
                .shippingCost(ctx.getShippingCost())
                .finalTotal(finalTotal)
                .itemDiscounts(itemDiscounts)
                .appliedPromos(ctx.getAppliedPromotions())
                .currency(ctx.getCurrency())
                .breakdownHash(breakdownHash)
                .calculatedAt(LocalDateTime.now())
                .build();

        ctx.setPriceBreakdown(breakdown);

        log.debug("Price breakdown built: subtotal={}, discount={}, tax={}, final={}, hash={}",
                ctx.getSubtotal().toDisplayString(), ctx.getTotalDiscount().toDisplayString(),
                ctx.getTaxAmount().toDisplayString(), finalTotal.toDisplayString(), breakdownHash);
    }

    private String buildDiscountReason(LineItemPricing item) {
        if (item.getAppliedDiscounts().isEmpty()) return "No discounts";
        return item.getAppliedDiscounts().stream()
                .map(d -> d.getType() + ": " + d.getReason())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");
    }
}
