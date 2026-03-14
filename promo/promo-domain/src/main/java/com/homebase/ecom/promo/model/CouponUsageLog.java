package com.homebase.ecom.promo.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class CouponUsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID usageId;

    @Column(nullable = false)
    private String couponCode;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private LocalDateTime usedAt;

    private LocalDateTime refundedAt;

    private String ipAddress;

    public CouponUsageLog() {
    }

    public UUID getUsageId() {
        return usageId;
    }

    public void setUsageId(UUID usageId) {
        this.usageId = usageId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(LocalDateTime usedAt) {
        this.usedAt = usedAt;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }

    public void setRefundedAt(LocalDateTime refundedAt) {
        this.refundedAt = refundedAt;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CouponUsageLog that = (CouponUsageLog) o;
        return java.util.Objects.equals(usageId, that.usageId) &&
                java.util.Objects.equals(couponCode, that.couponCode) &&
                java.util.Objects.equals(userId, that.userId) &&
                java.util.Objects.equals(orderId, that.orderId) &&
                java.util.Objects.equals(usedAt, that.usedAt) &&
                java.util.Objects.equals(refundedAt, that.refundedAt) &&
                java.util.Objects.equals(ipAddress, that.ipAddress);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(usageId, couponCode, userId, orderId, usedAt, refundedAt, ipAddress);
    }
}
