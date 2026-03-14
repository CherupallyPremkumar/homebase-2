package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class AppliedPromotion implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID promotionId;
    private String promotionName;
    private Money discountAmount;
    private BigDecimal discountPercent;
    private String strategy;
    private LocalDateTime appliedAt;

    public AppliedPromotion() {}

    private AppliedPromotion(Builder builder) {
        this.promotionId = builder.promotionId;
        this.promotionName = builder.promotionName;
        this.discountAmount = builder.discountAmount;
        this.discountPercent = builder.discountPercent;
        this.strategy = builder.strategy;
        this.appliedAt = builder.appliedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getPromotionId() { return promotionId; }
    public void setPromotionId(UUID promotionId) { this.promotionId = promotionId; }
    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }
    public Money getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Money discountAmount) { this.discountAmount = discountAmount; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public LocalDateTime getAppliedAt() { return appliedAt; }
    public void setAppliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppliedPromotion that = (AppliedPromotion) o;
        return Objects.equals(promotionId, that.promotionId) &&
                Objects.equals(promotionName, that.promotionName) &&
                Objects.equals(discountAmount, that.discountAmount) &&
                Objects.equals(discountPercent, that.discountPercent) &&
                Objects.equals(strategy, that.strategy) &&
                Objects.equals(appliedAt, that.appliedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotionId, promotionName, discountAmount, discountPercent, strategy, appliedAt);
    }

    @Override
    public String toString() {
        return "AppliedPromotion{" +
                "promotionId=" + promotionId +
                ", promotionName='" + promotionName + '\'' +
                ", discountAmount=" + discountAmount +
                ", discountPercent=" + discountPercent +
                ", strategy='" + strategy + '\'' +
                ", appliedAt=" + appliedAt +
                '}';
    }

    public static class Builder {
        private UUID promotionId;
        private String promotionName;
        private Money discountAmount;
        private BigDecimal discountPercent;
        private String strategy;
        private LocalDateTime appliedAt;

        public Builder promotionId(UUID promotionId) { this.promotionId = promotionId; return this; }
        public Builder promotionName(String promotionName) { this.promotionName = promotionName; return this; }
        public Builder discountAmount(Money discountAmount) { this.discountAmount = discountAmount; return this; }
        public Builder discountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; return this; }
        public Builder strategy(String strategy) { this.strategy = strategy; return this; }
        public Builder appliedAt(LocalDateTime appliedAt) { this.appliedAt = appliedAt; return this; }

        public AppliedPromotion build() {
            return new AppliedPromotion(this);
        }
    }
}
