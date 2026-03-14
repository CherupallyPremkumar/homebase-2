package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.util.List;

public class FreeShippingStrategy implements DiscountStrategy {
    private final Money minOrderValue;
    private final List<String> eligibleRegions;

    public FreeShippingStrategy(Money minOrderValue, List<String> eligibleRegions) {
        this.minOrderValue = minOrderValue;
        this.eligibleRegions = eligibleRegions;
    }

    public Money getMinOrderValue() {
        return minOrderValue;
    }

    public List<String> getEligibleRegions() {
        return eligibleRegions;
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        if (eligibleRegions != null && !eligibleRegions.contains(cart.getRegion())) {
            return false;
        }
        return cart.getTotalAmount().isGreaterThan(minOrderValue) ||
                cart.getTotalAmount().equals(minOrderValue);
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            return new Money(BigDecimal.ZERO, minOrderValue.getCurrency());
        }
        return cart.getShippingCost();
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                "Free Shipping",
                "Get free shipping for orders over " + minOrderValue
                        + (eligibleRegions != null ? " in selected regions" : ""));
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.FREE_SHIPPING;
    }
}
