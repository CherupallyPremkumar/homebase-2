package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ReturnWithOrderDetailQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String returnId;
    private String orderId;
    private String customerId;
    private String returnReason;
    private String returnType;
    private BigDecimal refundAmount;
    private String returnState;
    private Date returnCreatedTime;
    private String orderNumber;
    private BigDecimal orderAmount;
    private String orderCurrency;
    private String orderState;
    private Date orderCreatedTime;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String customerPhone;
    private String paymentStatus;
    private String paymentMethod;

    public String getReturnId() { return returnId; }
    public void setReturnId(String returnId) { this.returnId = returnId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
    public String getReturnType() { return returnType; }
    public void setReturnType(String returnType) { this.returnType = returnType; }
    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }
    public String getReturnState() { return returnState; }
    public void setReturnState(String returnState) { this.returnState = returnState; }
    public Date getReturnCreatedTime() { return returnCreatedTime; }
    public void setReturnCreatedTime(Date returnCreatedTime) { this.returnCreatedTime = returnCreatedTime; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getOrderCurrency() { return orderCurrency; }
    public void setOrderCurrency(String orderCurrency) { this.orderCurrency = orderCurrency; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public Date getOrderCreatedTime() { return orderCreatedTime; }
    public void setOrderCreatedTime(Date orderCreatedTime) { this.orderCreatedTime = orderCreatedTime; }
    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}
