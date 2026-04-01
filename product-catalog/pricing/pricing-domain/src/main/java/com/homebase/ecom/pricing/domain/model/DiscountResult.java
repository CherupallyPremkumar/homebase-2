package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;

/**
 * Single discount applied to a line item.
 * Records the amount, type, and reason for audit trail.
 */
public class DiscountResult {

    private Money amount;
    private String type;    // VOLUME, TIER, PROMO, COUPON
    private String reason;  // human-readable explanation
    private int percentOff; // the percentage applied (0 if flat)

    public DiscountResult() {}

    public DiscountResult(Money amount, String type, String reason, int percentOff) {
        this.amount = amount;
        this.type = type;
        this.reason = reason;
        this.percentOff = percentOff;
    }

    public Money getAmount() { return amount; }
    public void setAmount(Money amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public int getPercentOff() { return percentOff; }
    public void setPercentOff(int percentOff) { this.percentOff = percentOff; }
}
