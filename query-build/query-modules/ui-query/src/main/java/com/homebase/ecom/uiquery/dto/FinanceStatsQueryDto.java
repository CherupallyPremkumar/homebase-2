package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FinanceStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal totalRevenue;
    private BigDecimal todayRevenue;
    private long disbursedSettlements;
    private BigDecimal totalPayouts;
    private long pendingSettlements;
    private BigDecimal pendingPayoutAmount;
    private long disputedSettlements;
    private long totalRefunds;
    private BigDecimal totalRefundAmount;

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }
    public long getDisbursedSettlements() { return disbursedSettlements; }
    public void setDisbursedSettlements(long disbursedSettlements) { this.disbursedSettlements = disbursedSettlements; }
    public BigDecimal getTotalPayouts() { return totalPayouts; }
    public void setTotalPayouts(BigDecimal totalPayouts) { this.totalPayouts = totalPayouts; }
    public long getPendingSettlements() { return pendingSettlements; }
    public void setPendingSettlements(long pendingSettlements) { this.pendingSettlements = pendingSettlements; }
    public BigDecimal getPendingPayoutAmount() { return pendingPayoutAmount; }
    public void setPendingPayoutAmount(BigDecimal pendingPayoutAmount) { this.pendingPayoutAmount = pendingPayoutAmount; }
    public long getDisputedSettlements() { return disputedSettlements; }
    public void setDisputedSettlements(long disputedSettlements) { this.disputedSettlements = disputedSettlements; }
    public long getTotalRefunds() { return totalRefunds; }
    public void setTotalRefunds(long totalRefunds) { this.totalRefunds = totalRefunds; }
    public BigDecimal getTotalRefundAmount() { return totalRefundAmount; }
    public void setTotalRefundAmount(BigDecimal totalRefundAmount) { this.totalRefundAmount = totalRefundAmount; }
}
