package com.ecommerce.payment.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reconciliation_mismatches")
public class ReconciliationMismatch {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "gateway_type", nullable = false, length = 20)
    private String gatewayType = "stripe";

    @Column(name = "provider_transaction_id", nullable = false)
    private String providerTransactionId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "mismatch_type", nullable = false)
    private String mismatchType; // AMOUNT_MISMATCH, STATUS_MISMATCH, MISSING_IN_DB, MISSING_IN_STRIPE

    @Column(name = "provider_amount")
    private BigDecimal providerAmount;

    @Column(name = "internal_amount")
    private BigDecimal internalAmount;

    @Column(name = "provider_status")
    private String providerStatus;

    @Column(name = "internal_status")
    private String internalStatus;

    @Column(nullable = false)
    private boolean resolved = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolved_by")
    private String resolvedBy;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public String getProviderTransactionId() {
        return providerTransactionId;
    }

    public void setProviderTransactionId(String providerTransactionId) {
        this.providerTransactionId = providerTransactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMismatchType() {
        return mismatchType;
    }

    public void setMismatchType(String mismatchType) {
        this.mismatchType = mismatchType;
    }

    public BigDecimal getProviderAmount() {
        return providerAmount;
    }

    public void setProviderAmount(BigDecimal providerAmount) {
        this.providerAmount = providerAmount;
    }

    public BigDecimal getInternalAmount() {
        return internalAmount;
    }

    public void setInternalAmount(BigDecimal internalAmount) {
        this.internalAmount = internalAmount;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getInternalStatus() {
        return internalStatus;
    }

    public void setInternalStatus(String internalStatus) {
        this.internalStatus = internalStatus;
    }

    public boolean isResolved() {
        return resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolutionNotes() {
        return resolutionNotes;
    }

    public void setResolutionNotes(String resolutionNotes) {
        this.resolutionNotes = resolutionNotes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
