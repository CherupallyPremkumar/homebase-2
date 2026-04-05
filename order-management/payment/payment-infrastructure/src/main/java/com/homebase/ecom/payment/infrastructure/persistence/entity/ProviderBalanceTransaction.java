package com.homebase.ecom.payment.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A raw balance transaction record from the payment gateway.
 * Used as the source of truth during reconciliation and payout matching.
 * Extends BaseJpaEntity for audit fields (id, createdTime, lastModifiedTime, tenant, etc.)
 */
@Entity
@Table(name = "provider_balance_transactions", uniqueConstraints = {
        @UniqueConstraint(name = "ux_provider_balance_txn_gateway_provider_id", columnNames = {
                "gateway_type", "provider_balance_txn_id"
        })
}, indexes = {
        @Index(name = "idx_provider_balance_txn_gateway_occurred_at", columnList = "gateway_type,occurred_at"),
        @Index(name = "idx_provider_balance_txn_provider_payout_id", columnList = "provider_payout_id")
})
public class ProviderBalanceTransaction extends BaseJpaEntity {

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType;

    @Column(name = "provider_balance_txn_id", nullable = false, length = 255)
    private String providerBalanceTxnId;

    @Column(name = "txn_type", nullable = false, length = 50)
    private String txnType;

    @Column(name = "source_object_type", length = 50)
    private String sourceObjectType;

    @Column(name = "source_object_id", length = 255)
    private String sourceObjectId;

    @Column(name = "provider_payout_id", length = 255)
    private String providerPayoutId;

    @Column(length = 3, nullable = false)
    private String currency;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "fee_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal feeAmount = BigDecimal.ZERO;

    @Column(name = "net_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private String payload;

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getProviderBalanceTxnId() {
        return providerBalanceTxnId;
    }

    public void setProviderBalanceTxnId(String providerBalanceTxnId) {
        this.providerBalanceTxnId = providerBalanceTxnId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getSourceObjectType() {
        return sourceObjectType;
    }

    public void setSourceObjectType(String sourceObjectType) {
        this.sourceObjectType = sourceObjectType;
    }

    public String getSourceObjectId() {
        return sourceObjectId;
    }

    public void setSourceObjectId(String sourceObjectId) {
        this.sourceObjectId = sourceObjectId;
    }

    public String getProviderPayoutId() {
        return providerPayoutId;
    }

    public void setProviderPayoutId(String providerPayoutId) {
        this.providerPayoutId = providerPayoutId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public LocalDateTime getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(LocalDateTime occurredAt) {
        this.occurredAt = occurredAt;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
