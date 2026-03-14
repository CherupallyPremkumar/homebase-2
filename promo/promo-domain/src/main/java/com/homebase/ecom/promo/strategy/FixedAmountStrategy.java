package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;

public class FixedAmountStrategy implements DiscountStrategy {
    private final Money discountAmount;
    private final int minQuantity;

    public FixedAmountStrategy(Money discountAmount, int minQuantity) {
        this.discountAmount = discountAmount;
        this.minQuantity = minQuantity;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        return cart.getTotalItems() >= minQuantity;
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            return new Money(BigDecimal.ZERO, discountAmount.getCurrency());
        }
        return discountAmount.cap(cart.getTotalAmount());
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                discountAmount + " Off",
                "Fixed discount of " + discountAmount + " for orders with at least " + minQuantity + " items");
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.FIXED_AMOUNT;
    }
}
