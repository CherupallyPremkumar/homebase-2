package com.homebase.ecom.wms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FulfillmentWithOrderDetailQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fulfillmentId;
    private String orderId;
    private String fulfillmentState;
    private String priority;
    private String fulfillmentType;
    private int totalItems;
    private int totalWeightGrams;
    private String assignedPicker;
    private String assignedPacker;
    private Date pickStartedAt;
    private Date pickCompletedAt;
    private Date packStartedAt;
    private Date packCompletedAt;
    private Date shippedAt;
    private String carrier;
    private String trackingNumber;
    private String shippingMethod;
    private Date estimatedShipDate;
    private Date estimatedDeliveryDate;
    private String notes;
    private Date createdTime;
    private String warehouseId;
    private String warehouseName;
    private String warehouseCode;
    private String orderNumber;
    private String orderState;
    private BigDecimal orderAmount;
    private String orderCurrency;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String shippingStatus;
    private String currentLocation;

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
    public int getTotalWeightGrams() { return totalWeightGrams; }
    public void setTotalWeightGrams(int totalWeightGrams) { this.totalWeightGrams = totalWeightGrams; }
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
    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }
    public Date getEstimatedShipDate() { return estimatedShipDate; }
    public void setEstimatedShipDate(Date estimatedShipDate) { this.estimatedShipDate = estimatedShipDate; }
    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public String getWarehouseCode() { return warehouseCode; }
    public void setWarehouseCode(String warehouseCode) { this.warehouseCode = warehouseCode; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getOrderCurrency() { return orderCurrency; }
    public void setOrderCurrency(String orderCurrency) { this.orderCurrency = orderCurrency; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }
    public String getShippingStatus() { return shippingStatus; }
    public void setShippingStatus(String shippingStatus) { this.shippingStatus = shippingStatus; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
}
