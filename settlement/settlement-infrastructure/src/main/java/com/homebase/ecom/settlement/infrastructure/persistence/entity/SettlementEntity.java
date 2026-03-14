package com.homebase.ecom.settlement.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "settlements", indexes = {
        @Index(name = "idx_settlement_supplier", columnList = "supplier_id"),
        @Index(name = "idx_settlement_period", columnList = "period_month, period_year")
})
public class SettlementEntity extends AbstractJpaStateEntity {

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "period_month")
    private Integer periodMonth;

    @Column(name = "period_year")
    private Integer periodYear;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_sales_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalSalesAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "commission_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money commissionAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "net_payout_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money netPayoutAmount;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SettlementLineItemEntity> lineItems = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "settlement_id")
    private List<SettlementActivityLogEntity> activities = new ArrayList<>();

    // --- Getters & Setters ---

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getPeriodMonth() {
        return periodMonth;
    }

    public void setPeriodMonth(Integer periodMonth) {
        this.periodMonth = periodMonth;
    }

    public Integer getPeriodYear() {
        return periodYear;
    }

    public void setPeriodYear(Integer periodYear) {
        this.periodYear = periodYear;
    }

    public Money getTotalSalesAmount() {
        return totalSalesAmount;
    }

    public void setTotalSalesAmount(Money totalSalesAmount) {
        this.totalSalesAmount = totalSalesAmount;
    }

    public Money getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(Money commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public Money getNetPayoutAmount() {
        return netPayoutAmount;
    }

    public void setNetPayoutAmount(Money netPayoutAmount) {
        this.netPayoutAmount = netPayoutAmount;
    }

    public List<SettlementLineItemEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<SettlementLineItemEntity> lineItems) {
        this.lineItems = lineItems;
    }

    public List<SettlementActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<SettlementActivityLogEntity> activities) {
        this.activities = activities;
    }
}
