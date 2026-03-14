package com.homebase.ecom.settlement.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import com.homebase.ecom.shared.Money;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Settlement extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String description;
    private String supplierId;
    private Integer periodMonth;
    private Integer periodYear;
    private Money totalSalesAmount;
    private Money commissionAmount;
    private Money netPayoutAmount;
    private List<SettlementLineItem> lineItems = new ArrayList<>();

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public Integer getPeriodMonth() { return periodMonth; }
    public void setPeriodMonth(Integer periodMonth) { this.periodMonth = periodMonth; }

    public Integer getPeriodYear() { return periodYear; }
    public void setPeriodYear(Integer periodYear) { this.periodYear = periodYear; }

    public Money getTotalSalesAmount() { return totalSalesAmount; }
    public void setTotalSalesAmount(Money totalSalesAmount) { this.totalSalesAmount = totalSalesAmount; }

    public Money getCommissionAmount() { return commissionAmount; }
    public void setCommissionAmount(Money commissionAmount) { this.commissionAmount = commissionAmount; }

    public Money getNetPayoutAmount() { return netPayoutAmount; }
    public void setNetPayoutAmount(Money netPayoutAmount) { this.netPayoutAmount = netPayoutAmount; }

    public List<SettlementLineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<SettlementLineItem> lineItems) { this.lineItems = lineItems; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

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
