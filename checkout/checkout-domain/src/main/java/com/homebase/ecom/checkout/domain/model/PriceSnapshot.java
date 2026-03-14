package com.homebase.ecom.checkout.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceSnapshot {
    private final String lockToken;
    private final BigDecimal finalTotal;
    private final BigDecimal discountAmount;
    private final String currency;
    private final LocalDateTime lockedAt;

    public PriceSnapshot(String lockToken, BigDecimal finalTotal, BigDecimal discountAmount, String currency, LocalDateTime lockedAt) {
        this.lockToken = lockToken;
        this.finalTotal = finalTotal;
        this.discountAmount = discountAmount;
        this.currency = currency;
        this.lockedAt = lockedAt;
    }

    public String getLockToken() { return lockToken; }
    public BigDecimal getFinalTotal() { return finalTotal; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public String getCurrency() { return currency; }
    public LocalDateTime getLockedAt() { return lockedAt; }
}
