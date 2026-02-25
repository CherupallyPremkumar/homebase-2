package com.ecommerce.payment.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ledger_entries", indexes = {
        @Index(name = "idx_ledger_entries_account", columnList = "ledger_account_id"),
        @Index(name = "idx_ledger_entries_transaction", columnList = "transaction_id")
})
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "ledger_account_id", nullable = false)
    private String ledgerAccountId;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId; // Reference to PaymentTransaction or Refund ID

    @Column(nullable = false)
    private BigDecimal amount; // Positive for Debit, Negative for Credit

    @Column(length = 3)
    private String currency = "INR";

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

    public String getLedgerAccountId() {
        return ledgerAccountId;
    }

    public void setLedgerAccountId(String ledgerAccountId) {
        this.ledgerAccountId = ledgerAccountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
