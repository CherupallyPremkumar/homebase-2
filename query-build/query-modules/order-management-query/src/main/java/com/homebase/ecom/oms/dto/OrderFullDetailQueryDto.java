package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderFullDetailQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderNumber;
    private String orderState;
    private BigDecimal totalAmount;
    private String currency;
    private int itemCount;
    private Date orderCreatedTime;
    private Date orderModifiedTime;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhone;
    private String paymentId;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal paymentAmount;
    private String paymentCurrency;
    private String gatewayTransactionId;
    private String paymentFailureReason;
    private Date paymentTime;
    private String shipmentId;
    private String shippingStatus;
    private String carrier;
    private String trackingNumber;
    private String shippingMethod;
    private Date estimatedDeliveryDate;
    private Date actualDeliveryDate;
    private String currentLocation;
    private int deliveryAttempts;
    private String fulfillmentId;
    private String fulfillmentStatus;
    private String warehouseId;
    private String fulfillmentPriority;
    private String assignedPicker;
    private String assignedPacker;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
    public Date getOrderCreatedTime() { return orderCreatedTime; }
    public void setOrderCreatedTime(Date orderCreatedTime) { this.orderCreatedTime = orderCreatedTime; }
    public Date getOrderModifiedTime() { return orderModifiedTime; }
    public void setOrderModifiedTime(Date orderModifiedTime) { this.orderModifiedTime = orderModifiedTime; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
    public String getPaymentCurrency() { return paymentCurrency; }
    public void setPaymentCurrency(String paymentCurrency) { this.paymentCurrency = paymentCurrency; }
    public String getGatewayTransactionId() { return gatewayTransactionId; }
    public void setGatewayTransactionId(String gatewayTransactionId) { this.gatewayTransactionId = gatewayTransactionId; }
    public String getPaymentFailureReason() { return paymentFailureReason; }
    public void setPaymentFailureReason(String paymentFailureReason) { this.paymentFailureReason = paymentFailureReason; }
    public Date getPaymentTime() { return paymentTime; }
    public void setPaymentTime(Date paymentTime) { this.paymentTime = paymentTime; }
    public String getShipmentId() { return shipmentId; }
    public void setShipmentId(String shipmentId) { this.shipmentId = shipmentId; }
    public String getShippingStatus() { return shippingStatus; }
    public void setShippingStatus(String shippingStatus) { this.shippingStatus = shippingStatus; }
    public String getCarrier() { return carrier; }
    public void setCarrier(String carrier) { this.carrier = carrier; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getShippingMethod() { return shippingMethod; }
    public void setShippingMethod(String shippingMethod) { this.shippingMethod = shippingMethod; }
    public Date getEstimatedDeliveryDate() { return estimatedDeliveryDate; }
    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) { this.estimatedDeliveryDate = estimatedDeliveryDate; }
    public Date getActualDeliveryDate() { return actualDeliveryDate; }
    public void setActualDeliveryDate(Date actualDeliveryDate) { this.actualDeliveryDate = actualDeliveryDate; }
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public int getDeliveryAttempts() { return deliveryAttempts; }
    public void setDeliveryAttempts(int deliveryAttempts) { this.deliveryAttempts = deliveryAttempts; }
    public String getFulfillmentId() { return fulfillmentId; }
    public void setFulfillmentId(String fulfillmentId) { this.fulfillmentId = fulfillmentId; }
    public String getFulfillmentStatus() { return fulfillmentStatus; }
    public void setFulfillmentStatus(String fulfillmentStatus) { this.fulfillmentStatus = fulfillmentStatus; }
    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getFulfillmentPriority() { return fulfillmentPriority; }
    public void setFulfillmentPriority(String fulfillmentPriority) { this.fulfillmentPriority = fulfillmentPriority; }
    public String getAssignedPicker() { return assignedPicker; }
    public void setAssignedPicker(String assignedPicker) { this.assignedPicker = assignedPicker; }
    public String getAssignedPacker() { return assignedPacker; }
    public void setAssignedPacker(String assignedPacker) { this.assignedPacker = assignedPacker; }
}
