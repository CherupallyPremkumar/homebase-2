package com.homebase.ecom.payment.ledger.service;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Immutable ledger entry recording a single debit or credit line.
 * Positive amount = Debit, Negative amount = Credit.
 * Uses Money value object to keep amount and currency together.
 */
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
    private String transactionId;

    /**
     * The monetary value of this entry.
     * Positive = Debit, Negative = Credit.
     * Currency is carried by Money — must be set by caller via CurrencyResolver.
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "amount", nullable = false, precision = 10, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money money;

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

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
