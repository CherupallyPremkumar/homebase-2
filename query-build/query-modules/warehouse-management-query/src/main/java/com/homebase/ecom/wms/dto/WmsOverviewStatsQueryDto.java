package com.homebase.ecom.wms.dto;

import java.io.Serializable;

public class WmsOverviewStatsQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long activeWarehouses;
    private long totalSkus;
    private long lowStockItems;
    private long outOfStockItems;
    private long pendingFulfillments;
    private long shippedToday;
    private long pendingPickLists;

    public long getActiveWarehouses() { return activeWarehouses; }
    public void setActiveWarehouses(long activeWarehouses) { this.activeWarehouses = activeWarehouses; }
    public long getTotalSkus() { return totalSkus; }
    public void setTotalSkus(long totalSkus) { this.totalSkus = totalSkus; }
    public long getLowStockItems() { return lowStockItems; }
    public void setLowStockItems(long lowStockItems) { this.lowStockItems = lowStockItems; }
    public long getOutOfStockItems() { return outOfStockItems; }
    public void setOutOfStockItems(long outOfStockItems) { this.outOfStockItems = outOfStockItems; }
    public long getPendingFulfillments() { return pendingFulfillments; }
    public void setPendingFulfillments(long pendingFulfillments) { this.pendingFulfillments = pendingFulfillments; }
    public long getShippedToday() { return shippedToday; }
    public void setShippedToday(long shippedToday) { this.shippedToday = shippedToday; }
    public long getPendingPickLists() { return pendingPickLists; }
    public void setPendingPickLists(long pendingPickLists) { this.pendingPickLists = pendingPickLists; }
}
