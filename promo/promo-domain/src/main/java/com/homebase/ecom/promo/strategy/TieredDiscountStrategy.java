package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.math.RoundingMode;
import java.util.List;

public class TieredDiscountStrategy implements DiscountStrategy {
    private List<PriceTier> tiers;

    public TieredDiscountStrategy() {
    }

    public TieredDiscountStrategy(List<PriceTier> tiers) {
        this.tiers = tiers;
    }

    public List<PriceTier> getTiers() {
        return tiers;
    }

    public void setTiers(List<PriceTier> tiers) {
        this.tiers = tiers;
    }

    public static class PriceTier implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Money minAmount;
        private final BigDecimal discountPercent;

        public PriceTier(Money minAmount, BigDecimal discountPercent) {
            this.minAmount = minAmount;
            this.discountPercent = discountPercent;
        }

        public Money getMinAmount() {
            return minAmount;
        }

        public BigDecimal getDiscountPercent() {
            return discountPercent;
        }
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        if (tiers == null || tiers.isEmpty())
            return false;
        Money minTierAmount = tiers.stream().map(PriceTier::getMinAmount).min(Comparator.comparing(Money::getAmount))
                .get();
        return cart.getTotalAmount().isGreaterThan(minTierAmount) || cart.getTotalAmount().equals(minTierAmount);
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            String currency = tiers.get(0).getMinAmount().getCurrency();
            return new Money(BigDecimal.ZERO, currency);
        }

        Money total = cart.getTotalAmount();
        PriceTier applicableTier = tiers.stream()
                .filter(tier -> total.isGreaterThan(tier.getMinAmount()) || total.equals(tier.getMinAmount()))
                .max(Comparator.comparing(tier -> tier.getMinAmount().getAmount()))
                .orElse(null);

        if (applicableTier == null)
            return new Money(BigDecimal.ZERO, total.getCurrency());

        return total.multiply(
                applicableTier.getDiscountPercent().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                "Tiered Discount",
                "Get higher discounts as you shop more! Up to " +
                        tiers.stream().map(PriceTier::getDiscountPercent).max(BigDecimal::compareTo).get() + "% off");
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.TIERED_DISCOUNT;
    }
}
