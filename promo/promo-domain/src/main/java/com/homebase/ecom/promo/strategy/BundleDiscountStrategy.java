package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.CartSnapshot.CartSnapshotItem;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BundleDiscountStrategy implements DiscountStrategy {
    private final List<String> requiredProductIds;
    private final Money bundleDiscount;

    public BundleDiscountStrategy(List<String> requiredProductIds, Money bundleDiscount) {
        this.requiredProductIds = requiredProductIds;
        this.bundleDiscount = bundleDiscount;
    }

    public List<String> getRequiredProductIds() {
        return requiredProductIds;
    }

    public Money getBundleDiscount() {
        return bundleDiscount;
    }

    @Override
    public boolean isEligible(CartSnapshot cart) {
        if (requiredProductIds == null || requiredProductIds.isEmpty())
            return false;
        Set<String> cartProductIds = cart.getItems().stream()
                .map(CartSnapshotItem::getProductId)
                .collect(Collectors.toSet());
        return cartProductIds.containsAll(requiredProductIds);
    }

    @Override
    public Money calculateSavings(CartSnapshot cart) {
        if (!isEligible(cart)) {
            return new Money(BigDecimal.ZERO, bundleDiscount.getCurrency());
        }

        // Find how many full bundles are in the cart
        long maxBundles = requiredProductIds.stream()
                .mapToLong(pid -> cart.getItems().stream()
                        .filter(item -> pid.equals(item.getProductId()))
                        .mapToInt(CartSnapshotItem::getQuantity)
                        .sum())
                .min()
                .orElse(0);

        return bundleDiscount.multiply(BigDecimal.valueOf(maxBundles));
    }

    @Override
    public PromotionDetail getPromotionDetails() {
        return new PromotionDetail(
                "Bundle Savings",
                "Save " + bundleDiscount + " when you buy these products together");
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.BUNDLE_DISCOUNT;
    }
}
