package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.CartSnapshot.CartSnapshotItem;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

public class BuyXGetYStrategy implements DiscountStrategy {
    private final int requiredQuantity;
    private final int freeQuantity;
    private final String category;

    public BuyXGetYStrategy(int requiredQuantity, int freeQuantity, String category) {
        this.requiredQuantity = requiredQuantity;
        this.freeQuantity = freeQuantity;
        this.category = category;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        return cart.getQuantityByCategory(category) >= (requiredQuantity + freeQuantity);
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            String currency = "INR";
            if (cart.getItems() != null && !cart.getItems().isEmpty()) {
                currency = cart.getItems().get(0).getPrice().getCurrency();
            }
            return Money.zero(currency);
        }

        int totalQuantity = cart.getQuantityByCategory(category);
        int sets = totalQuantity / (requiredQuantity + freeQuantity);
        int eligibleFreeItems = sets * freeQuantity;

        // Find the lowest price items in this category to discount
        return cart.getItems().stream()
                .filter(item -> category.equals(item.getCategory()))
                .map(CartSnapshotItem::getPrice)
                .sorted((p1, p2) -> Long.compare(p1.getAmount(), p2.getAmount()))
                .limit(eligibleFreeItems)
                .reduce(Money.zero(cart.getItems().get(0).getPrice().getCurrency()),
                        (total, price) -> total.add(price));
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                "Buy " + requiredQuantity + " Get " + freeQuantity + " Free",
                "Buy " + requiredQuantity + " items in " + category + " and get " + freeQuantity + " more for free");
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.BUY_X_GET_Y;
    }
}
