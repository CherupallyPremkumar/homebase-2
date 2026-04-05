package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TicketWithOrderContextQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String ticketId;
    private String subject;
    private String ticketState;
    private String priority;
    private String category;
    private String assignedAgentId;
    private Date ticketCreatedTime;
    private String customerId;
    private String customerFirstName;
    private String customerLastName;
    private String customerEmail;
    private String orderId;
    private String orderNumber;
    private String orderState;
    private BigDecimal orderAmount;
    private String orderCurrency;
    private String paymentStatus;
    private String shippingStatus;
    private String trackingNumber;

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getTicketState() { return ticketState; }
    public void setTicketState(String ticketState) { this.ticketState = ticketState; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getAssignedAgentId() { return assignedAgentId; }
    public void setAssignedAgentId(String assignedAgentId) { this.assignedAgentId = assignedAgentId; }
    public Date getTicketCreatedTime() { return ticketCreatedTime; }
    public void setTicketCreatedTime(Date ticketCreatedTime) { this.ticketCreatedTime = ticketCreatedTime; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public String getCustomerFirstName() { return customerFirstName; }
    public void setCustomerFirstName(String customerFirstName) { this.customerFirstName = customerFirstName; }
    public String getCustomerLastName() { return customerLastName; }
    public void setCustomerLastName(String customerLastName) { this.customerLastName = customerLastName; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public String getOrderCurrency() { return orderCurrency; }
    public void setOrderCurrency(String orderCurrency) { this.orderCurrency = orderCurrency; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getShippingStatus() { return shippingStatus; }
    public void setShippingStatus(String shippingStatus) { this.shippingStatus = shippingStatus; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
}
