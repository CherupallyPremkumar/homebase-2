package com.homebase.ecom.promo.strategy;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.PromotionDetail;
import com.homebase.ecom.promo.model.StrategyType;
import com.homebase.ecom.shared.Money;

public interface DiscountStrategy {
    boolean isEligible(CartSnapshot cart);
    Money calculateSavings(CartSnapshot cart);
    PromotionDetail getPromotionDetails();
    StrategyType getStrategyType();
}
