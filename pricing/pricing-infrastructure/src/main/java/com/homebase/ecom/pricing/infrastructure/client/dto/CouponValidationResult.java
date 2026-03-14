package com.homebase.ecom.pricing.infrastructure.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CouponValidationResult {
    private boolean isValid;
    private String couponCode;
    private String promotionId;
    private String promotionName;
    private BigDecimal savingsAmount;
    private String strategy;
    private LocalDateTime expiryDate;
    private Integer remainingUsage;
    private boolean singleUsePerCustomer;
    private String reason;
    private String errorMessage;

    public CouponValidationResult() {}

    public CouponValidationResult(boolean isValid, String couponCode, String promotionId, String promotionName,
                                  BigDecimal savingsAmount, String strategy, LocalDateTime expiryDate,
                                  Integer remainingUsage, boolean singleUsePerCustomer, String reason, String errorMessage) {
        this.isValid = isValid;
        this.couponCode = couponCode;
        this.promotionId = promotionId;
        this.promotionName = promotionName;
        this.savingsAmount = savingsAmount;
        this.strategy = strategy;
        this.expiryDate = expiryDate;
        this.remainingUsage = remainingUsage;
        this.singleUsePerCustomer = singleUsePerCustomer;
        this.reason = reason;
        this.errorMessage = errorMessage;
    }

    private CouponValidationResult(Builder builder) {
        this.isValid = builder.isValid;
        this.couponCode = builder.couponCode;
        this.promotionId = builder.promotionId;
        this.promotionName = builder.promotionName;
        this.savingsAmount = builder.savingsAmount;
        this.strategy = builder.strategy;
        this.expiryDate = builder.expiryDate;
        this.remainingUsage = builder.remainingUsage;
        this.singleUsePerCustomer = builder.singleUsePerCustomer;
        this.reason = builder.reason;
        this.errorMessage = builder.errorMessage;
    }

    public static Builder builder() {
        return new Builder();
    }

    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getPromotionId() { return promotionId; }
    public void setPromotionId(String promotionId) { this.promotionId = promotionId; }
    public String getPromotionName() { return promotionName; }
    public void setPromotionName(String promotionName) { this.promotionName = promotionName; }
    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    public Integer getRemainingUsage() { return remainingUsage; }
    public void setRemainingUsage(Integer remainingUsage) { this.remainingUsage = remainingUsage; }
    public boolean isSingleUsePerCustomer() { return singleUsePerCustomer; }
    public void setSingleUsePerCustomer(boolean singleUsePerCustomer) { this.singleUsePerCustomer = singleUsePerCustomer; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponValidationResult that = (CouponValidationResult) o;
        return isValid == that.isValid &&
                singleUsePerCustomer == that.singleUsePerCustomer &&
                Objects.equals(couponCode, that.couponCode) &&
                Objects.equals(promotionId, that.promotionId) &&
                Objects.equals(promotionName, that.promotionName) &&
                Objects.equals(savingsAmount, that.savingsAmount) &&
                Objects.equals(strategy, that.strategy) &&
                Objects.equals(expiryDate, that.expiryDate) &&
                Objects.equals(remainingUsage, that.remainingUsage) &&
                Objects.equals(reason, that.reason) &&
                Objects.equals(errorMessage, that.errorMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isValid, couponCode, promotionId, promotionName, savingsAmount,
                strategy, expiryDate, remainingUsage, singleUsePerCustomer, reason, errorMessage);
    }

    @Override
    public String toString() {
        return "CouponValidationResult{" +
                "isValid=" + isValid +
                ", couponCode='" + couponCode + '\'' +
                ", promotionId='" + promotionId + '\'' +
                ", promotionName='" + promotionName + '\'' +
                ", savingsAmount=" + savingsAmount +
                ", strategy='" + strategy + '\'' +
                ", expiryDate=" + expiryDate +
                ", remainingUsage=" + remainingUsage +
                ", singleUsePerCustomer=" + singleUsePerCustomer +
                ", reason='" + reason + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public static class Builder {
        private boolean isValid;
        private String couponCode;
        private String promotionId;
        private String promotionName;
        private BigDecimal savingsAmount;
        private String strategy;
        private LocalDateTime expiryDate;
        private Integer remainingUsage;
        private boolean singleUsePerCustomer;
        private String reason;
        private String errorMessage;

        public Builder isValid(boolean isValid) { this.isValid = isValid; return this; }
        public Builder couponCode(String couponCode) { this.couponCode = couponCode; return this; }
        public Builder promotionId(String promotionId) { this.promotionId = promotionId; return this; }
        public Builder promotionName(String promotionName) { this.promotionName = promotionName; return this; }
        public Builder savingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; return this; }
        public Builder strategy(String strategy) { this.strategy = strategy; return this; }
        public Builder expiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; return this; }
        public Builder remainingUsage(Integer remainingUsage) { this.remainingUsage = remainingUsage; return this; }
        public Builder singleUsePerCustomer(boolean singleUsePerCustomer) { this.singleUsePerCustomer = singleUsePerCustomer; return this; }
        public Builder reason(String reason) { this.reason = reason; return this; }
        public Builder errorMessage(String errorMessage) { this.errorMessage = errorMessage; return this; }

        public CouponValidationResult build() {
            return new CouponValidationResult(this);
        }
    }
}
