package com.homebase.ecom.settlement.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.time.LocalDate;
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

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "commission_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money commissionAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "platform_fee")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money platformFee;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "net_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money netAmount;

    @Column(name = "settlement_period_start")
    private LocalDate settlementPeriodStart;

    @Column(name = "settlement_period_end")
    private LocalDate settlementPeriodEnd;

    @Column(name = "disbursement_reference")
    private String disbursementReference;

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

    public Money getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(Money commissionAmount) { this.commissionAmount = commissionAmount; }

    public Money getPlatformFee() { return platformFee; }
    public void setPlatformFee(Money platformFee) { this.platformFee = platformFee; }

    public Money getNetAmount() { return netAmount; }
    public void setNetAmount(Money netAmount) { this.netAmount = netAmount; }

    public LocalDate getSettlementPeriodStart() { return settlementPeriodStart; }
    public void setSettlementPeriodStart(LocalDate settlementPeriodStart) { this.settlementPeriodStart = settlementPeriodStart; }

    public LocalDate getSettlementPeriodEnd() { return settlementPeriodEnd; }
    public void setSettlementPeriodEnd(LocalDate settlementPeriodEnd) { this.settlementPeriodEnd = settlementPeriodEnd; }

    public String getDisbursementReference() { return disbursementReference; }
    public void setDisbursementReference(String disbursementReference) { this.disbursementReference = disbursementReference; }

    public List<SettlementAdjustmentEntity> getAdjustments() { return adjustments; }
    public void setAdjustments(List<SettlementAdjustmentEntity> adjustments) { this.adjustments = adjustments; }

    public List<SettlementActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<SettlementActivityLogEntity> activities) { this.activities = activities; }
}
