package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PercentageDiscountStrategy implements DiscountStrategy {
    private final BigDecimal discountPercent;
    private final Money minPurchaseAmount;
    private final Money maxDiscount;

    public PercentageDiscountStrategy(BigDecimal discountPercent, Money minPurchaseAmount, Money maxDiscount) {
        this.discountPercent = discountPercent;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscount = maxDiscount;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public Money getMinPurchaseAmount() {
        return minPurchaseAmount;
    }

    public Money getMaxDiscount() {
        return maxDiscount;
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        if (minPurchaseAmount == null)
            return true;
        return cart.getTotalAmount().isGreaterThan(minPurchaseAmount) ||
                cart.getTotalAmount().equals(minPurchaseAmount);
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            return Money.zero(cart.getTotalAmount().getCurrency());
        }
        Money total = cart.getTotalAmount();
        // Calculate discount: amount * percent / 100
        long discountAmount = BigDecimal.valueOf(total.getAmount())
                .multiply(discountPercent)
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP)
                .longValue();
        Money discount = Money.of(discountAmount, total.getCurrency());

        if (maxDiscount != null) {
            return discount.cap(maxDiscount);
        }
        return discount;
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                discountPercent + "% Off",
                "Get " + discountPercent + "% off on your order above " + minPurchaseAmount);
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.PERCENTAGE_DISCOUNT;
    }
}
