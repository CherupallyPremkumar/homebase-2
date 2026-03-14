package com.homebase.ecom.reconciliation.dto;

import java.math.BigDecimal;

public class MismatchDto {

    public enum MismatchType {
        AMOUNT_MISMATCH,
        MISSING_IN_SYSTEM,
        MISSING_IN_GATEWAY
    }

    private String transactionId;
    private String orderId;
    private BigDecimal gatewayAmount;
    private BigDecimal systemAmount;
    private MismatchType mismatchType;

    public MismatchDto() {
    }

    public MismatchDto(String transactionId, String orderId, BigDecimal gatewayAmount,
                       BigDecimal systemAmount, MismatchType mismatchType) {
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.gatewayAmount = gatewayAmount;
        this.systemAmount = systemAmount;
        this.mismatchType = mismatchType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getGatewayAmount() {
        return gatewayAmount;
    }

    public void setGatewayAmount(BigDecimal gatewayAmount) {
        this.gatewayAmount = gatewayAmount;
    }

    public BigDecimal getSystemAmount() {
        return systemAmount;
    }

    public void setSystemAmount(BigDecimal systemAmount) {
        this.systemAmount = systemAmount;
    }

    public MismatchType getMismatchType() {
        return mismatchType;
    }

    public void setMismatchType(MismatchType mismatchType) {
        this.mismatchType = mismatchType;
    }
}
