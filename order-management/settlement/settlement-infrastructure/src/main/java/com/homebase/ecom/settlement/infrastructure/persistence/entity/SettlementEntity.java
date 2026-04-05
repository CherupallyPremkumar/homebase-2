package com.homebase.ecom.settlement.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "settlements", indexes = {
        @Index(name = "idx_settlement_supplier", columnList = "supplier_id"),
        @Index(name = "idx_settlement_order", columnList = "order_id"),
        @Index(name = "idx_settlement_period", columnList = "settlement_period_start, settlement_period_end")
})
public class SettlementEntity extends AbstractJpaStateEntity {

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "order_id")
    private String orderId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "order_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money orderAmount;

    @Column(name = "commission_amount")
    private Long commissionAmountPaise;

    @Column(name = "platform_fee")
    private Long platformFeePaise;

    @Column(name = "net_amount")
    private Long netAmountPaise;

    @Column(name = "settlement_period_start")
    private LocalDate settlementPeriodStart;

    @Column(name = "settlement_period_end")
    private LocalDate settlementPeriodEnd;

    @Column(name = "disbursement_reference")
    private String disbursementReference;

    // --- Fields from changeset settlement-004 ---

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "settlement_number", length = 100)
    private String settlementNumber;

    @Column(name = "tax_amount")
    private Long taxAmountPaise;

    @Column(name = "adjustment_amount")
    private Long adjustmentAmountPaise;

    @Column(name = "bank_account_id")
    private String bankAccountId;

    @Column(name = "disbursement_date")
    private LocalDateTime disbursementDate;

    @Column(name = "disbursement_method", length = 50)
    private String disbursementMethod;

    // --- Child collections ---

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "settlement_id")
    private List<SettlementAdjustmentEntity> adjustments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "settlement_id")
    private List<SettlementActivityLogEntity> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public Money getOrderAmount() { return orderAmount; }
    public void setOrderAmount(Money orderAmount) { this.orderAmount = orderAmount; }

    public Long getCommissionAmountPaise() { return commissionAmountPaise; }
    public void setCommissionAmountPaise(Long commissionAmountPaise) { this.commissionAmountPaise = commissionAmountPaise; }

    public Long getPlatformFeePaise() { return platformFeePaise; }
    public void setPlatformFeePaise(Long platformFeePaise) { this.platformFeePaise = platformFeePaise; }

    public Long getNetAmountPaise() { return netAmountPaise; }
    public void setNetAmountPaise(Long netAmountPaise) { this.netAmountPaise = netAmountPaise; }

    public LocalDate getSettlementPeriodStart() { return settlementPeriodStart; }
    public void setSettlementPeriodStart(LocalDate settlementPeriodStart) { this.settlementPeriodStart = settlementPeriodStart; }

    public LocalDate getSettlementPeriodEnd() { return settlementPeriodEnd; }
    public void setSettlementPeriodEnd(LocalDate settlementPeriodEnd) { this.settlementPeriodEnd = settlementPeriodEnd; }

    public String getDisbursementReference() { return disbursementReference; }
    public void setDisbursementReference(String disbursementReference) { this.disbursementReference = disbursementReference; }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getSettlementNumber() { return settlementNumber; }
    public void setSettlementNumber(String settlementNumber) { this.settlementNumber = settlementNumber; }

    public Long getTaxAmountPaise() { return taxAmountPaise; }
    public void setTaxAmountPaise(Long taxAmountPaise) { this.taxAmountPaise = taxAmountPaise; }

    public Long getAdjustmentAmountPaise() { return adjustmentAmountPaise; }
    public void setAdjustmentAmountPaise(Long adjustmentAmountPaise) { this.adjustmentAmountPaise = adjustmentAmountPaise; }

    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }

    public LocalDateTime getDisbursementDate() { return disbursementDate; }
    public void setDisbursementDate(LocalDateTime disbursementDate) { this.disbursementDate = disbursementDate; }

    public String getDisbursementMethod() { return disbursementMethod; }
    public void setDisbursementMethod(String disbursementMethod) { this.disbursementMethod = disbursementMethod; }

    public List<SettlementAdjustmentEntity> getAdjustments() { return adjustments; }
    public void setAdjustments(List<SettlementAdjustmentEntity> adjustments) { this.adjustments = adjustments; }

    public List<SettlementActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<SettlementActivityLogEntity> activities) { this.activities = activities; }
}
