package com.homebase.ecom.disputes.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Dispute extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // Core fields
    public String orderId;
    public String customerId;
    public String sellerId;
    public String disputeType; // Product Quality, Not Delivered, Wrong Item, Damaged, Refund Issue, Seller Misconduct
    public String reason;
    public BigDecimal disputedAmount;
    public String priority; // High, Medium, Low
    public String assignedTo;
    private String tenant;

    // Customer / Seller communication
    public String customerComplaint;
    public String sellerResponse;
    public String evidenceLinks; // JSON array

    // Investigation fields
    public String investigatorId;
    public String investigationNotes;

    // Resolution fields
    public String resolutionOutcome; // REFUND_CUSTOMER, SIDE_WITH_SELLER, PARTIAL_REFUND, SPLIT_DECISION
    public String resolutionNotes;
    public LocalDateTime resolutionDate;
    public BigDecimal refundAmount;

    // SLA
    public int slaTargetDays = 5;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public TransientMap getTransientMap() { return this.transientMap; }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        DisputeActivityLog activityLog = new DisputeActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
