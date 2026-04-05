package com.homebase.ecom.pricing.domain.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class LockedPriceBreakdown implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID orderId;
    private final PriceBreakdown priceBreakdown;
    private final LocalDateTime lockedAt;
    private final LocalDateTime lockedUntil;
    private final String lockToken;
    private final int lockDurationMinutes;
    private final String lockSignature;

    public LockedPriceBreakdown(UUID orderId, PriceBreakdown priceBreakdown, String lockToken, int lockDurationMinutes, String lockSignature) {
        this.orderId = orderId;
        this.priceBreakdown = priceBreakdown;
        this.lockToken = lockToken;
        this.lockDurationMinutes = lockDurationMinutes;
        this.lockSignature = lockSignature;
        this.lockedAt = LocalDateTime.now();
        this.lockedUntil = this.lockedAt.plusMinutes(lockDurationMinutes);
    }

    public UUID getOrderId() { return orderId; }
    public PriceBreakdown getPriceBreakdown() { return priceBreakdown; }
    public LocalDateTime getLockedAt() { return lockedAt; }
    public LocalDateTime getLockedUntil() { return lockedUntil; }
    public String getLockToken() { return lockToken; }
    public int getLockDurationMinutes() { return lockDurationMinutes; }
    public String getLockSignature() { return lockSignature; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(lockedUntil);
    }
}
