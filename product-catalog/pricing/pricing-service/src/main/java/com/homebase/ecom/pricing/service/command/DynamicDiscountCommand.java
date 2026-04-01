package com.homebase.ecom.pricing.service.command;

import com.homebase.ecom.pricing.domain.model.*;
import com.homebase.ecom.pricing.domain.port.PolicyEvaluationPort;
import com.homebase.ecom.pricing.domain.port.PolicyEvaluationPort.PolicyDecision;
import com.homebase.ecom.shared.Money;
import org.chenile.owiz.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dynamic discount engine — delegates rule evaluation to the centralized Policy service.
 * Policy module holds all rules with SpEL conditions + metadata (discountAction, discountValue, discountScope).
 * Pricing just sends facts and applies whatever decisions come back.
 *
 * Amazon pattern (smaller version): one centralized rules engine, all services call it.
 * Add new discount types by creating rules in Policy DB — zero code deploy.
 */
public class DynamicDiscountCommand implements Command<PricingContext> {

    private static final Logger log = LoggerFactory.getLogger(DynamicDiscountCommand.class);

    // Metadata keys returned by rules-engine decisions
    static final String META_DISCOUNT_SCOPE = "discountScope";
    static final String META_DISCOUNT_ACTION = "discountAction";
    static final String META_DISCOUNT_VALUE = "discountValue";
    static final String META_TARGET_VARIANT_ID = "targetVariantId";

    // Supported actions
    static final String ACTION_PERCENTAGE = "PERCENTAGE";
    static final String ACTION_FLAT = "FLAT";

    // Supported scopes
    static final String SCOPE_LINE_ITEM = "LINE_ITEM";
    static final String SCOPE_CART_LEVEL = "CART_LEVEL";

    private final PolicyEvaluationPort policyEvaluationPort;

    public DynamicDiscountCommand(PolicyEvaluationPort policyEvaluationPort) {
        this.policyEvaluationPort = policyEvaluationPort;
    }

    @Override
    public void execute(PricingContext ctx) throws Exception {
        Map<String, Object> facts = buildFacts(ctx);
        List<PolicyDecision> decisions = policyEvaluationPort.evaluateDiscountRules(facts);

        if (decisions == null || decisions.isEmpty()) {
            log.debug("No dynamic discount rules matched for cart {}", ctx.getCartId());
            return;
        }

        for (PolicyDecision decision : decisions) {
            if (!"ALLOW".equals(decision.getEffect())) continue;

            String scope = decision.getMetaValue(META_DISCOUNT_SCOPE, SCOPE_CART_LEVEL);
            String action = decision.getMetaValue(META_DISCOUNT_ACTION, ACTION_PERCENTAGE);
            String valueStr = decision.getMetaValue(META_DISCOUNT_VALUE, "0");

            long value;
            try {
                value = Long.parseLong(valueStr);
            } catch (NumberFormatException e) {
                log.warn("Skipping rule '{}': invalid discountValue '{}' — expected numeric",
                        decision.getRuleId(), valueStr);
                continue;
            }

            try {
                if (SCOPE_LINE_ITEM.equals(scope)) {
                    applyLineItemDiscount(decision, action, value, ctx);
                } else {
                    applyCartLevelDiscount(decision, action, value, ctx);
                }
            } catch (Exception e) {
                log.warn("Failed to apply policy decision '{}': {}", decision.getRuleId(), e.getMessage());
            }
        }
    }

    private void applyCartLevelDiscount(PolicyDecision decision, String action, long value, PricingContext ctx) {
        Money discount = calculateDiscount(action, value, ctx.getSubtotal(), ctx.getCurrency());
        if (discount.isPositive()) {
            ctx.setTotalDiscount(ctx.getTotalDiscount().add(discount));

            ctx.getAppliedPromotions().add(AppliedPromotion.builder()
                    .promotionName(decision.getRuleName())
                    .discountAmount(discount)
                    .strategy("DYNAMIC_" + action)
                    .appliedAt(java.time.LocalDateTime.now())
                    .build());

            log.info("Policy rule '{}' applied (cart-level): -{}", decision.getRuleId(), discount.toDisplayString());
        }
    }

    private void applyLineItemDiscount(PolicyDecision decision, String action, long value, PricingContext ctx) {
        String targetVariant = decision.getMetaValue(META_TARGET_VARIANT_ID);

        for (LineItemPricing item : ctx.getLineItems()) {
            // If rule targets a specific variant, skip non-matching items
            if (targetVariant != null && !targetVariant.equals(item.getVariantId())) continue;

            Money discountPerUnit = calculateDiscount(action, value, item.getCurrentPrice(), ctx.getCurrency());
            if (discountPerUnit.isPositive()) {
                Money newPrice = item.getCurrentPrice().subtract(discountPerUnit);
                if (newPrice.isNegative()) newPrice = Money.zero(ctx.getCurrency());
                item.setCurrentPrice(newPrice);

                Money totalDiscount = discountPerUnit.multiply(item.getQuantity());
                item.setLineDiscount(item.getLineDiscount().add(totalDiscount));
                item.addDiscount(new DiscountResult(totalDiscount, "DYNAMIC",
                        decision.getRuleName(),
                        ACTION_PERCENTAGE.equals(action) ? (int) value : 0));

                log.info("Policy rule '{}' applied to variant {}: -{}",
                        decision.getRuleId(), item.getVariantId(), totalDiscount.toDisplayString());
            }
        }
    }

    private Money calculateDiscount(String action, long value, Money base, String currency) {
        if (ACTION_PERCENTAGE.equals(action)) {
            return Money.of(Math.round((double) base.getAmount() * value / 100), currency);
        } else if (ACTION_FLAT.equals(action)) {
            Money flat = Money.of(value, currency);
            return flat.isGreaterThan(base) ? base : flat;
        }
        return Money.zero(currency);
    }

    /**
     * Build facts map sent to Policy engine for SpEL evaluation.
     * These are the variables available in rule conditions.
     */
    private Map<String, Object> buildFacts(PricingContext ctx) {
        Map<String, Object> facts = new HashMap<>();
        facts.put("subtotal", ctx.getSubtotal() != null ? ctx.getSubtotal().getAmount() : 0L);
        facts.put("currency", ctx.getCurrency());
        facts.put("userId", ctx.getUserId());
        facts.put("customerTier", ctx.getCustomerTier() != null ? ctx.getCustomerTier() : "REGULAR");
        facts.put("region", ctx.getRegion() != null ? ctx.getRegion() : "");
        facts.put("itemCount", ctx.getLineItems().size());
        facts.put("totalQuantity", ctx.getLineItems().stream().mapToInt(LineItemPricing::getQuantity).sum());

        Set<String> variantIds = ctx.getLineItems().stream()
                .map(LineItemPricing::getVariantId).collect(Collectors.toSet());
        facts.put("variantIds", variantIds);

        Set<String> productIds = ctx.getLineItems().stream()
                .map(LineItemPricing::getProductId).filter(p -> p != null).collect(Collectors.toSet());
        facts.put("productIds", productIds);

        Set<String> sellerIds = ctx.getLineItems().stream()
                .map(LineItemPricing::getSellerId).filter(s -> s != null).collect(Collectors.toSet());
        facts.put("sellerIds", sellerIds);

        return facts;
    }
}
