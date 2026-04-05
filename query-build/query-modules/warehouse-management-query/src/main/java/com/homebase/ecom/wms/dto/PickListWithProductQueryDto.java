package com.homebase.ecom.wms.dto;

import java.io.Serializable;
import java.util.Date;

public class PickListWithProductQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String pickListId;
    private String fulfillmentOrderId;
    private String warehouseId;
    private String assignedTo;
    private String pickStatus;
    private int itemCount;
    private int pickedCount;
    private Date startedAt;
    private Date completedAt;
    private String notes;
    private Date createdTime;
    private String warehouseName;
    private String warehouseCode;
    private String orderId;
    private String fulfillmentState;
    private String fulfillmentPriority;
    private int fulfillmentTotalItems;
    private String orderNumber;
    private String customerId;

    public String getPickListId() { return pickListId; }
    public void setPickListId(String pickListId) { this.pickListId = pickListId; }
    public String getFulfillmentOrderId() { return fulfillmentOrderId; }
    public void setFulfillmentOrderId(String fulfillmentOrderId) { this.fulfillmentOrderId = fulfillmentOrderId; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
    public String getPickStatus() { return pickStatus; }
    public void setPickStatus(String pickStatus) { this.pickStatus = pickStatus; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    public int getPickedCount() { return pickedCount; }
    public void setPickedCount(int pickedCount) { this.pickedCount = pickedCount; }
    public Date getStartedAt() { return startedAt; }
    public void setStartedAt(Date startedAt) { this.startedAt = startedAt; }
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getFulfillmentState() { return fulfillmentState; }
    public void setFulfillmentState(String fulfillmentState) { this.fulfillmentState = fulfillmentState; }
    public String getFulfillmentPriority() { return fulfillmentPriority; }
    public void setFulfillmentPriority(String fulfillmentPriority) { this.fulfillmentPriority = fulfillmentPriority; }
    public int getFulfillmentTotalItems() { return fulfillmentTotalItems; }
    public void setFulfillmentTotalItems(int fulfillmentTotalItems) { this.fulfillmentTotalItems = fulfillmentTotalItems; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
