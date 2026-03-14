package com.homebase.ecom.promo.dto;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.util.List;

public class PricingResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private PromotionResultDto bestDeal;
    private Money totalSavings;
    private List<PromotionResultDto> allApplicablePromotions;

    public PricingResponse() {
    }

    public PromotionResultDto getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(PromotionResultDto bestDeal) {
        this.bestDeal = bestDeal;
    }

    public Money getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(Money totalSavings) {
        this.totalSavings = totalSavings;
    }

    public List<PromotionResultDto> getAllApplicablePromotions() {
        return allApplicablePromotions;
    }

    public void setAllApplicablePromotions(List<PromotionResultDto> allApplicablePromotions) {
        this.allApplicablePromotions = allApplicablePromotions;
    }
}
