package com.homebase.ecom.inventory.dto;

import java.io.Serializable;

public class FulfillmentCenterSummaryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fulfillmentCenter;
    private int itemCount;
    private long totalQuantity;
    private long totalAvailable;
    private long totalReserved;
    private long totalDamaged;

    public String getFulfillmentCenter() { return fulfillmentCenter; }
    public void setFulfillmentCenter(String fulfillmentCenter) { this.fulfillmentCenter = fulfillmentCenter; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    public long getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(long totalQuantity) { this.totalQuantity = totalQuantity; }
    public long getTotalAvailable() { return totalAvailable; }
    public void setTotalAvailable(long totalAvailable) { this.totalAvailable = totalAvailable; }
    public long getTotalReserved() { return totalReserved; }
    public void setTotalReserved(long totalReserved) { this.totalReserved = totalReserved; }
    public long getTotalDamaged() { return totalDamaged; }
    public void setTotalDamaged(long totalDamaged) { this.totalDamaged = totalDamaged; }
}
