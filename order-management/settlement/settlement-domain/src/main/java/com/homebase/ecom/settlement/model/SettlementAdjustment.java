package com.homebase.ecom.settlement.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents an adjustment to a settlement (e.g., refund deduction, dispute resolution).
 */
public class SettlementAdjustment {

    private String id;
    private BigDecimal amount;
    private String reason;
    private String adjustedBy;
    private LocalDateTime adjustedAt;

    public SettlementAdjustment() {}

    public SettlementAdjustment(BigDecimal amount, String reason, String adjustedBy) {
        this.amount = amount;
        this.reason = reason;
        this.adjustedBy = adjustedBy;
        this.adjustedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getAdjustedBy() { return adjustedBy; }
    public void setAdjustedBy(String adjustedBy) { this.adjustedBy = adjustedBy; }

    public LocalDateTime getAdjustedAt() { return adjustedAt; }
    public void setAdjustedAt(LocalDateTime adjustedAt) { this.adjustedAt = adjustedAt; }

    private String tenant;
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
