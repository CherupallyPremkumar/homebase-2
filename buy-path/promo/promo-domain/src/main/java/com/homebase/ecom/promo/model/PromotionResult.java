package com.homebase.ecom.promo.model;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;

public class PromotionResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Promotion promotion;
    private Money savings;
    private boolean applicable;

    public PromotionResult() {
    }

    public PromotionResult(Promotion promotion, Money savings, boolean applicable) {
        this.promotion = promotion;
        this.savings = savings;
        this.applicable = applicable;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public Money getSavings() {
        return savings;
    }

    public void setSavings(Money savings) {
        this.savings = savings;
    }

    public boolean isApplicable() {
        return applicable;
    }

    public void setApplicable(boolean applicable) {
        this.applicable = applicable;
    }

    public static PromotionResult notApplicable(Promotion promotion) {
        return new PromotionResult(promotion, Money.zero("INR"), false);
    }

    public static PromotionResult notSatisfied(Promotion promotion) {
        return new PromotionResult(promotion, Money.zero("INR"), false);
    }
}
