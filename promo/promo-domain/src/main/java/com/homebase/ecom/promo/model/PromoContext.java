package com.homebase.ecom.promo.model;

import com.homebase.ecom.promo.model.CartSnapshot;
import com.homebase.ecom.promo.model.Promotion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PromoContext implements Serializable {
    private static final long serialVersionUID = 1L;

    private CartSnapshot cart;
    private List<Promotion> eligiblePromotions = new ArrayList<>();
    private List<PromotionResult> evaluationResults = new ArrayList<>();
    private PromotionResult bestDeal;
    private String couponCode;

    public PromoContext() {
    }

    public PromoContext(CartSnapshot cart, List<Promotion> eligiblePromotions, List<PromotionResult> evaluationResults,
            PromotionResult bestDeal, String couponCode) {
        this.cart = cart;
        this.eligiblePromotions = eligiblePromotions != null ? eligiblePromotions : new ArrayList<>();
        this.evaluationResults = evaluationResults != null ? evaluationResults : new ArrayList<>();
        this.bestDeal = bestDeal;
        this.couponCode = couponCode;
    }

    public CartSnapshot getCart() {
        return cart;
    }

    public void setCart(CartSnapshot cart) {
        this.cart = cart;
    }

    public List<Promotion> getEligiblePromotions() {
        return eligiblePromotions;
    }

    public void setEligiblePromotions(List<Promotion> eligiblePromotions) {
        this.eligiblePromotions = eligiblePromotions;
    }

    public List<PromotionResult> getEvaluationResults() {
        return evaluationResults;
    }

    public void setEvaluationResults(List<PromotionResult> evaluationResults) {
        this.evaluationResults = evaluationResults;
    }

    public PromotionResult getBestDeal() {
        return bestDeal;
    }

    public void setBestDeal(PromotionResult bestDeal) {
        this.bestDeal = bestDeal;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CartSnapshot cart;
        private List<Promotion> eligiblePromotions = new ArrayList<>();
        private List<PromotionResult> evaluationResults = new ArrayList<>();
        private PromotionResult bestDeal;
        private String couponCode;

        public Builder cart(CartSnapshot cart) {
            this.cart = cart;
            return this;
        }

        public Builder eligiblePromotions(List<Promotion> eligiblePromotions) {
            this.eligiblePromotions = eligiblePromotions;
            return this;
        }

        public Builder evaluationResults(List<PromotionResult> evaluationResults) {
            this.evaluationResults = evaluationResults;
            return this;
        }

        public Builder bestDeal(PromotionResult bestDeal) {
            this.bestDeal = bestDeal;
            return this;
        }

        public Builder couponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }

        public PromoContext build() {
            return new PromoContext(cart, eligiblePromotions, evaluationResults, bestDeal, couponCode);
        }
    }

    public void addEligiblePromotion(Promotion promotion) {
        this.eligiblePromotions.add(promotion);
    }

    public void addEvaluationResult(PromotionResult result) {
        this.evaluationResults.add(result);
    }

    public List<PromotionResult> getApplicableResults() {
        return evaluationResults.stream()
                .filter(PromotionResult::isApplicable)
                .collect(Collectors.toList());
    }
}
