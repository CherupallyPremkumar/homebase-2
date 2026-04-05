package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SettlementWithBreakdownQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String settlementId;
    private String orderId;
    private String supplierId;
    private String settlementState;
    private BigDecimal orderAmount;
    private BigDecimal commissionAmount;
    private BigDecimal commissionRate;
    private BigDecimal platformFee;
    private BigDecimal tdsAmount;
    private BigDecimal gstOnCommission;
    private BigDecimal netAmount;
    private String currency;
    private Date periodStart;
    private Date periodEnd;
    private Date settlementCreatedTime;
    private String orderNumber;
    private String orderState;
    private BigDecimal orderTotal;
    private Date orderCreatedTime;
    private String supplierName;
    private String paymentStatus;
    private String paymentMethod;
    private BigDecimal paymentAmount;

    public String getSettlementId() { return settlementId; }
    public void setSettlementId(String settlementId) { this.settlementId = settlementId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getSettlementState() { return settlementState; }
    public void setSettlementState(String settlementState) { this.settlementState = settlementState; }
    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public void setCommissionRate(BigDecimal commissionRate) { this.commissionRate = commissionRate; }
    public BigDecimal getPlatformFee() { return platformFee; }
    public void setPlatformFee(BigDecimal platformFee) { this.platformFee = platformFee; }
    public BigDecimal getTdsAmount() { return tdsAmount; }
    public void setTdsAmount(BigDecimal tdsAmount) { this.tdsAmount = tdsAmount; }
    public BigDecimal getGstOnCommission() { return gstOnCommission; }
    public void setGstOnCommission(BigDecimal gstOnCommission) { this.gstOnCommission = gstOnCommission; }
    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getPeriodStart() { return periodStart; }
    public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }
    public Date getPeriodEnd() { return periodEnd; }
    public void setPeriodEnd(Date periodEnd) { this.periodEnd = periodEnd; }
    public Date getSettlementCreatedTime() { return settlementCreatedTime; }
    public void setSettlementCreatedTime(Date settlementCreatedTime) { this.settlementCreatedTime = settlementCreatedTime; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getOrderTotal() { return orderTotal; }
    public void setOrderTotal(BigDecimal orderTotal) { this.orderTotal = orderTotal; }
    public Date getOrderCreatedTime() { return orderCreatedTime; }
    public void setOrderCreatedTime(Date orderCreatedTime) { this.orderCreatedTime = orderCreatedTime; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getPaymentAmount() { return paymentAmount; }
    public void setPaymentAmount(BigDecimal paymentAmount) { this.paymentAmount = paymentAmount; }
}
