package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class OmsOverviewStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long totalActiveOrders;
    private long pendingOrders;
    private long shippedOrders;
    private long deliveredOrders;
    private long cancelledOrders;
    private long openReturns;
    private long openTickets;
    private long failedPayments;
    private long pendingSettlements;
    private BigDecimal todayRevenue;

    public long getTotalActiveOrders() { return totalActiveOrders; }
    public void setTotalActiveOrders(long totalActiveOrders) { this.totalActiveOrders = totalActiveOrders; }
    public long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(long pendingOrders) { this.pendingOrders = pendingOrders; }
    public long getShippedOrders() { return shippedOrders; }
    public void setShippedOrders(long shippedOrders) { this.shippedOrders = shippedOrders; }
    public long getDeliveredOrders() { return deliveredOrders; }
    public void setDeliveredOrders(long deliveredOrders) { this.deliveredOrders = deliveredOrders; }
    public long getCancelledOrders() { return cancelledOrders; }
    public void setCancelledOrders(long cancelledOrders) { this.cancelledOrders = cancelledOrders; }
    public long getOpenReturns() { return openReturns; }
    public void setOpenReturns(long openReturns) { this.openReturns = openReturns; }
    public long getOpenTickets() { return openTickets; }
    public void setOpenTickets(long openTickets) { this.openTickets = openTickets; }
    public long getFailedPayments() { return failedPayments; }
    public void setFailedPayments(long failedPayments) { this.failedPayments = failedPayments; }
    public long getPendingSettlements() { return pendingSettlements; }
    public void setPendingSettlements(long pendingSettlements) { this.pendingSettlements = pendingSettlements; }
    public BigDecimal getTodayRevenue() { return todayRevenue; }
    public void setTodayRevenue(BigDecimal todayRevenue) { this.todayRevenue = todayRevenue; }
}
