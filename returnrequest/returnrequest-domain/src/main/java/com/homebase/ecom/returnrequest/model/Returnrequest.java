package com.homebase.ecom.returnrequest.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Returnrequest extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // Core fields
    public String orderId;
    public String customerId;
    public List<ReturnItem> items = new ArrayList<>();
    public String reason;
    public String returnType; // REFUND, EXCHANGE, STORE_CREDIT
    public BigDecimal totalRefundAmount;
    public BigDecimal restockingFee;
    public String description;

    // Review fields
    public String reviewerId;
    public String reviewNotes;
    public String rejectionReason;
    public String rejectionComment;

    // Warehouse fields
    public String warehouseId;
    public String conditionOnReceipt;
    public String inspectorId;
    public String inspectorNotes;

    // Order context (set from order.events)
    public LocalDateTime orderDeliveryDate;
    public BigDecimal orderTotalValue;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public TransientMap getTransientMap() { return this.transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        ReturnrequestActivityLog activityLog = new ReturnrequestActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
