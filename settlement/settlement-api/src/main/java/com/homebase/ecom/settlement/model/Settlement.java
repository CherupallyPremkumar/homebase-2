package com.homebase.ecom.settlement.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.shared.model.Money;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "settlements", indexes = {
        @Index(name = "idx_settlement_supplier", columnList = "supplier_id"),
        @Index(name = "idx_settlement_period", columnList = "period_month, period_year")
})
public class Settlement extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Column(name = "period_month", nullable = false)
    private Integer periodMonth;

    @Column(name = "period_year", nullable = false)
    private Integer periodYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SettlementStatus status = SettlementStatus.CALCULATED;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_sales_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money totalSalesAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "commission_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money commissionAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "net_payout_amount", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money netPayoutAmount;

    @OneToMany(mappedBy = "settlement", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SettlementLineItem> lineItems = new ArrayList<>();

    @Transient
    public TransientMap transientMap = new TransientMap();

    // Activities for STM
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "settlement_id")
    public List<SettlementActivityLog> activities = new ArrayList<>();

    // --- Getters & Setters ---

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

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
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

    public List<SettlementLineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<SettlementLineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public TransientMap getTransientMap() {
        return transientMap;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SettlementActivityLog log = new SettlementActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        activities.add(log);
        return log;
    }
}
