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

    public String orderId;
    public String orderItemId;
    public String reason;
    public Integer quantity = 1;
    public BigDecimal refundAmount;
    public String returnType;
    public String description;

    // Business fields for return processing
    public BigDecimal itemPrice;
    public LocalDateTime orderDeliveryDate;
    public String inspectorId;
    public String inspectorNotes;
    public String rejectionReason;
    public String rejectionComment;
    public String pickupTrackingNumber;
    public String warehouseId;
    public String conditionOnReceipt;
    public String refundMethod;
    public String refundTransactionId;
    public LocalDateTime refundProcessedAt;

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
