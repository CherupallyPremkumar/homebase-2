package com.homebase.ecom.settlement.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class SettlementDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String supplierId;
    private String orderId;
    private String stateId;
    private BigDecimal orderAmount;
    private BigDecimal commissionAmount;
    private BigDecimal platformFee;
    private BigDecimal netAmount;
    private String currency;
    private LocalDate settlementPeriodStart;
    private LocalDate settlementPeriodEnd;
    private String disbursementReference;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public BigDecimal getOrderAmount() { return orderAmount; }
    public void setOrderAmount(BigDecimal orderAmount) { this.orderAmount = orderAmount; }

    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(BigDecimal commissionAmount) { this.commissionAmount = commissionAmount; }

    public BigDecimal getPlatformFee() { return platformFee; }
    public void setPlatformFee(BigDecimal platformFee) { this.platformFee = platformFee; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getSettlementPeriodStart() { return settlementPeriodStart; }
    public void setSettlementPeriodStart(LocalDate settlementPeriodStart) { this.settlementPeriodStart = settlementPeriodStart; }

    public LocalDate getSettlementPeriodEnd() { return settlementPeriodEnd; }
    public void setSettlementPeriodEnd(LocalDate settlementPeriodEnd) { this.settlementPeriodEnd = settlementPeriodEnd; }

    public String getDisbursementReference() { return disbursementReference; }
    public void setDisbursementReference(String disbursementReference) { this.disbursementReference = disbursementReference; }
}
