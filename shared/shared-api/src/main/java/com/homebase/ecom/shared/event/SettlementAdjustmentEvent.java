package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Event published when a refund requires settlement adjustment.
 * Consumed by: settlement BC to adjust supplier payout records.
 */
public class SettlementAdjustmentEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private BigDecimal adjustmentAmount;
    private String currency;
    private String reason;
    private LocalDateTime adjustedAt;

    public SettlementAdjustmentEvent() {
    }

    public SettlementAdjustmentEvent(String orderId, BigDecimal adjustmentAmount, String currency,
            String reason, LocalDateTime adjustedAt) {
        this.orderId = orderId;
        this.adjustmentAmount = adjustmentAmount;
        this.currency = currency;
        this.reason = reason;
        this.adjustedAt = adjustedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getAdjustmentAmount() { return adjustmentAmount; }
    public void setAdjustmentAmount(BigDecimal adjustmentAmount) { this.adjustmentAmount = adjustmentAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getAdjustedAt() { return adjustedAt; }
    public void setAdjustedAt(LocalDateTime adjustedAt) { this.adjustedAt = adjustedAt; }
}
