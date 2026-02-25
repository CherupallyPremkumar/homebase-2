package com.ecommerce.payment.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payout_lines", indexes = {
        @Index(name = "idx_payout_lines_payout_id", columnList = "payout_id"),
        @Index(name = "idx_payout_lines_provider_balance_txn_id", columnList = "provider_balance_txn_id"),
        @Index(name = "idx_payout_lines_internal_payment_txn", columnList = "internal_payment_transaction_id"),
        @Index(name = "idx_payout_lines_internal_refund", columnList = "internal_refund_id")
})
public class PayoutLine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "payout_id", nullable = false)
    private String payoutId;

    @Column(name = "provider_balance_txn_id", length = 255)
    private String providerBalanceTxnId;

    @Column(name = "internal_payment_transaction_id", length = 36)
    private String internalPaymentTransactionId;

    @Column(name = "internal_refund_id", length = 255)
    private String internalRefundId;

    @Column(name = "line_type", nullable = false, length = 50)
    private String lineType;

    @Column(name = "gross_amount", nullable = false)
    private BigDecimal grossAmount = BigDecimal.ZERO;

    @Column(name = "fee_amount", nullable = false)
    private BigDecimal feeAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(length = 3, nullable = false)
    private String currency = "INR";

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }

    public String getProviderBalanceTxnId() {
        return providerBalanceTxnId;
    }

    public void setProviderBalanceTxnId(String providerBalanceTxnId) {
        this.providerBalanceTxnId = providerBalanceTxnId;
    }

    public String getInternalPaymentTransactionId() {
        return internalPaymentTransactionId;
    }

    public void setInternalPaymentTransactionId(String internalPaymentTransactionId) {
        this.internalPaymentTransactionId = internalPaymentTransactionId;
    }

    public String getInternalRefundId() {
        return internalRefundId;
    }

    public void setInternalRefundId(String internalRefundId) {
        this.internalRefundId = internalRefundId;
    }

    public String getLineType() {
        return lineType;
    }

    public void setLineType(String lineType) {
        this.lineType = lineType;
    }

    public BigDecimal getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(BigDecimal grossAmount) {
        this.grossAmount = grossAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
