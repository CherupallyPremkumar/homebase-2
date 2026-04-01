package com.homebase.ecom.pricing.infrastructure.event;

import com.homebase.ecom.shared.Money;
import java.time.LocalDateTime;
import java.util.Objects;

public class PriceLockedEvent {
    private String eventId;
    private String orderId;
    private String cartId;
    private String userId;
    private LocalDateTime occurredAt;

    private Money finalTotal;
    private Money totalDiscount;
    private String breakdownHash;

    private String lockToken;
    private LocalDateTime lockedUntil;
    private int lockDurationMinutes;

    private String appliedCouponCode;
    private String appliedPromotionId;

    public PriceLockedEvent() {}

    private PriceLockedEvent(Builder builder) {
        this.eventId = builder.eventId;
        this.orderId = builder.orderId;
        this.cartId = builder.cartId;
        this.userId = builder.userId;
        this.occurredAt = builder.occurredAt;
        this.finalTotal = builder.finalTotal;
        this.totalDiscount = builder.totalDiscount;
        this.breakdownHash = builder.breakdownHash;
        this.lockToken = builder.lockToken;
        this.lockedUntil = builder.lockedUntil;
        this.lockDurationMinutes = builder.lockDurationMinutes;
        this.appliedCouponCode = builder.appliedCouponCode;
        this.appliedPromotionId = builder.appliedPromotionId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
    public Money getFinalTotal() { return finalTotal; }
    public void setFinalTotal(Money finalTotal) { this.finalTotal = finalTotal; }
    public Money getTotalDiscount() { return totalDiscount; }
    public void setTotalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; }
    public String getBreakdownHash() { return breakdownHash; }
    public void setBreakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; }
    public String getLockToken() { return lockToken; }
    public void setLockToken(String lockToken) { this.lockToken = lockToken; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; }
    public int getLockDurationMinutes() { return lockDurationMinutes; }
    public void setLockDurationMinutes(int lockDurationMinutes) { this.lockDurationMinutes = lockDurationMinutes; }
    public String getAppliedCouponCode() { return appliedCouponCode; }
    public void setAppliedCouponCode(String appliedCouponCode) { this.appliedCouponCode = appliedCouponCode; }
    public String getAppliedPromotionId() { return appliedPromotionId; }
    public void setAppliedPromotionId(String appliedPromotionId) { this.appliedPromotionId = appliedPromotionId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceLockedEvent that = (PriceLockedEvent) o;
        return lockDurationMinutes == that.lockDurationMinutes &&
                Objects.equals(eventId, that.eventId) &&
                Objects.equals(orderId, that.orderId) &&
                Objects.equals(cartId, that.cartId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(occurredAt, that.occurredAt) &&
                Objects.equals(finalTotal, that.finalTotal) &&
                Objects.equals(totalDiscount, that.totalDiscount) &&
                Objects.equals(breakdownHash, that.breakdownHash) &&
                Objects.equals(lockToken, that.lockToken) &&
                Objects.equals(lockedUntil, that.lockedUntil) &&
                Objects.equals(appliedCouponCode, that.appliedCouponCode) &&
                Objects.equals(appliedPromotionId, that.appliedPromotionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, orderId, cartId, userId, occurredAt, finalTotal,
                totalDiscount, breakdownHash, lockToken, lockedUntil, lockDurationMinutes,
                appliedCouponCode, appliedPromotionId);
    }

    @Override
    public String toString() {
        return "PriceLockedEvent{" +
                "eventId='" + eventId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", cartId='" + cartId + '\'' +
                ", userId='" + userId + '\'' +
                ", occurredAt=" + occurredAt +
                ", finalTotal=" + finalTotal +
                ", totalDiscount=" + totalDiscount +
                ", breakdownHash='" + breakdownHash + '\'' +
                ", lockToken='" + lockToken + '\'' +
                ", lockedUntil=" + lockedUntil +
                ", lockDurationMinutes=" + lockDurationMinutes +
                ", appliedCouponCode='" + appliedCouponCode + '\'' +
                ", appliedPromotionId='" + appliedPromotionId + '\'' +
                '}';
    }

    public static class Builder {
        private String eventId;
        private String orderId;
        private String cartId;
        private String userId;
        private LocalDateTime occurredAt;
        private Money finalTotal;
        private Money totalDiscount;
        private String breakdownHash;
        private String lockToken;
        private LocalDateTime lockedUntil;
        private int lockDurationMinutes;
        private String appliedCouponCode;
        private String appliedPromotionId;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder orderId(String orderId) { this.orderId = orderId; return this; }
        public Builder cartId(String cartId) { this.cartId = cartId; return this; }
        public Builder userId(String userId) { this.userId = userId; return this; }
        public Builder occurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; return this; }
        public Builder finalTotal(Money finalTotal) { this.finalTotal = finalTotal; return this; }
        public Builder totalDiscount(Money totalDiscount) { this.totalDiscount = totalDiscount; return this; }
        public Builder breakdownHash(String breakdownHash) { this.breakdownHash = breakdownHash; return this; }
        public Builder lockToken(String lockToken) { this.lockToken = lockToken; return this; }
        public Builder lockedUntil(LocalDateTime lockedUntil) { this.lockedUntil = lockedUntil; return this; }
        public Builder lockDurationMinutes(int lockDurationMinutes) { this.lockDurationMinutes = lockDurationMinutes; return this; }
        public Builder appliedCouponCode(String appliedCouponCode) { this.appliedCouponCode = appliedCouponCode; return this; }
        public Builder appliedPromotionId(String appliedPromotionId) { this.appliedPromotionId = appliedPromotionId; return this; }

        public PriceLockedEvent build() {
            return new PriceLockedEvent(this);
        }
    }
}
