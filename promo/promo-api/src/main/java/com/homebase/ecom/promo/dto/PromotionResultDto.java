package com.homebase.ecom.promo.dto;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;

public class PromotionResultDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private PromotionDto promotion;
    private Money savings;

    public PromotionResultDto() {
    }

    public PromotionDto getPromotion() {
        return promotion;
    }

    public void setPromotion(PromotionDto promotion) {
        this.promotion = promotion;
    }

    public Money getSavings() {
        return savings;
    }

    public void setSavings(Money savings) {
        this.savings = savings;
    }
}
