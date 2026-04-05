package com.homebase.ecom.fulfillment.dto;

import java.io.Serializable;
import java.util.Date;

public class FulfillmentDetailDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private String warehouseId;
    private String userId;
    private String sagaId;
    private String priority;
    private String fulfillmentType;
    private String assignedPickerId;
    private String assignedPackerId;
    private Date pickStartedAt;
    private Date pickCompletedAt;
    private Date packStartedAt;
    private Date packCompletedAt;
    private Date shippedAt;
    private Date deliveredAt;
    private Date cancelledAt;
    private String cancellationReason;
    private int totalItems;
    private int totalWeightGrams;
    private String shippingMethod;
    private String carrier;
    private String trackingNumber;
    private Date estimatedShipDate;
    private Date estimatedDeliveryDate;
    private String notes;
    private String stateId;
    private String flowId;
    private Date createdTime;
    private Date lastModifiedTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSagaId() { return sagaId; }
    public void setSagaId(String sagaId) { this.sagaId = sagaId; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getFulfillmentType() { return fulfillmentType; }
    public void setFulfillmentType(String fulfillmentType) { this.fulfillmentType = fulfillmentType; }
    public String getAssignedPickerId() { return assignedPickerId; }
    public void setAssignedPickerId(String assignedPickerId) { this.assignedPickerId = assignedPickerId; }
    public String getAssignedPackerId() { return assignedPackerId; }
    public void setAssignedPackerId(String assignedPackerId) { this.assignedPackerId = assignedPackerId; }
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
    public Date getDeliveredAt() { return deliveredAt; }
    public void setDeliveredAt(Date deliveredAt) { this.deliveredAt = deliveredAt; }
    public Date getCancelledAt() { return cancelledAt; }
    public void setCancelledAt(Date cancelledAt) { this.cancelledAt = cancelledAt; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public int getTotalWeightGrams() { return totalWeightGrams; }
    public void setTotalWeightGrams(int totalWeightGrams) { this.totalWeightGrams = totalWeightGrams; }
    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public Date getEstimatedShipDate() { return estimatedShipDate; }
    public void setEstimatedShipDate(Date estimatedShipDate) { this.estimatedShipDate = estimatedShipDate; }
    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
}
