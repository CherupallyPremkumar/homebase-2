package com.homebase.ecom.pricing.infrastructure.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class CouponUsedEvent {
    private String eventId;
    private String couponCode;
    private String userId;
    private String orderId;
    private LocalDateTime usedAt;
    private BigDecimal savingsAmount;
    private String promotionId;
    private String paymentIntentId;

    public CouponUsedEvent() {}

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public LocalDateTime getUsedAt() { return usedAt; }
    public void setUsedAt(LocalDateTime usedAt) { this.usedAt = usedAt; }
    public BigDecimal getSavingsAmount() { return savingsAmount; }
    public void setSavingsAmount(BigDecimal savingsAmount) { this.savingsAmount = savingsAmount; }
    public String getPromotionId() { return promotionId; }
    public void setPromotionId(String promotionId) { this.promotionId = promotionId; }
    public String getPaymentIntentId() { return paymentIntentId; }
    public void setPaymentIntentId(String paymentIntentId) { this.paymentIntentId = paymentIntentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CouponUsedEvent that = (CouponUsedEvent) o;
        return Objects.equals(eventId, that.eventId) &&
                Objects.equals(couponCode, that.couponCode) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(usedAt, that.usedAt) &&
                Objects.equals(savingsAmount, that.savingsAmount) &&
                Objects.equals(promotionId, that.promotionId) &&
                Objects.equals(paymentIntentId, that.paymentIntentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, couponCode, userId, orderId, usedAt, savingsAmount, promotionId, paymentIntentId);
    }

    @Override
    public String toString() {
        return "CouponUsedEvent{" +
                "eventId='" + eventId + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", usedAt=" + usedAt +
                ", savingsAmount=" + savingsAmount +
                ", promotionId='" + promotionId + '\'' +
                ", paymentIntentId='" + paymentIntentId + '\'' +
                '}';
    }
}
