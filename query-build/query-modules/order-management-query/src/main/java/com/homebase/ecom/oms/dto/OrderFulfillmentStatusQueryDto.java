package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderFulfillmentStatusQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fulfillmentId;
    private String orderId;
    private String fulfillmentState;
    private String priority;
    private String fulfillmentType;
    private int totalItems;
    private String assignedPicker;
    private String assignedPacker;
    private Date pickStartedAt;
    private Date pickCompletedAt;
    private Date packStartedAt;
    private Date packCompletedAt;
    private Date shippedAt;
    private String carrier;
    private String trackingNumber;
    private Date estimatedShipDate;
    private Date estimatedDeliveryDate;
    private Date fulfillmentCreatedTime;
    private String warehouseId;
    private String warehouseName;
    private String warehouseCode;
    private String warehouseCity;
    private String orderNumber;
    private String orderState;
    private BigDecimal orderAmount;
    private String customerId;

    public String getFulfillmentId() { return fulfillmentId; }
    public void setFulfillmentId(String fulfillmentId) { this.fulfillmentId = fulfillmentId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getFulfillmentState() { return fulfillmentState; }
    public void setFulfillmentState(String fulfillmentState) { this.fulfillmentState = fulfillmentState; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getFulfillmentType() { return fulfillmentType; }
    public void setFulfillmentType(String fulfillmentType) { this.fulfillmentType = fulfillmentType; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public String getAssignedPicker() { return assignedPicker; }
    public void setAssignedPicker(String assignedPicker) { this.assignedPicker = assignedPicker; }
    public String getAssignedPacker() { return assignedPacker; }
    public void setAssignedPacker(String assignedPacker) { this.assignedPacker = assignedPacker; }
    public Date getPickStartedAt() { return pickStartedAt; }
    public void setPickStartedAt(Date pickStartedAt) { this.pickStartedAt = pickStartedAt; }
    public Date getPickCompletedAt() { return pickCompletedAt; }
    public void setPickCompletedAt(Date pickCompletedAt) { this.pickCompletedAt = pickCompletedAt; }
    public Date getPackStartedAt() { return packStartedAt; }
    public void setPackStartedAt(Date packStartedAt) { this.packStartedAt = packStartedAt; }
    public Date getPackCompletedAt() { return packCompletedAt; }
    public void setPackCompletedAt(Date packCompletedAt) { this.packCompletedAt = packCompletedAt; }
    public Date getShippedAt() { return shippedAt; }
    public void setShippedAt(Date shippedAt) { this.shippedAt = shippedAt; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public Date getEstimatedShipDate() { return estimatedShipDate; }
    public void setEstimatedShipDate(Date estimatedShipDate) { this.estimatedShipDate = estimatedShipDate; }
    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public Date getFulfillmentCreatedTime() { return fulfillmentCreatedTime; }
    public void setFulfillmentCreatedTime(Date fulfillmentCreatedTime) { this.fulfillmentCreatedTime = fulfillmentCreatedTime; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getWarehouseCity() { return warehouseCity; }
    public void setWarehouseCity(String warehouseCity) { this.warehouseCity = warehouseCity; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
}
