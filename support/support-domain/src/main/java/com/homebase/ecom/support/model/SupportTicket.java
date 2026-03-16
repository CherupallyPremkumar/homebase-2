package com.homebase.ecom.support.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Domain model for Support Ticket.
 *
 * Fields: id, customerId, orderId, category, priority, subject, description,
 * assignedAgentId, messages, reopenCount, stateId, flowId.
 */
public class SupportTicket extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String customerId;
    private String orderId;
    private String subject;
    private String category;    // ORDER, PAYMENT, SHIPPING, PRODUCT, ACCOUNT
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT
    private String description;
    private String assignedAgentId;
    private Date resolvedAt;
    private int reopenCount = 0;

    /** Flag for auto-state CHECK_SLA — set by scheduled SLA checker */
    private boolean slaBreached = false;
    /** Flag for auto-state CHECK_AUTO_CLOSE — set by scheduled auto-close checker */
    private boolean autoCloseReady = false;

    private List<TicketMessage> messages = new ArrayList<>();

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // --- customerId (replaces userId) ---
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAssignedAgentId() { return assignedAgentId; }
    public void setAssignedAgentId(String assignedAgentId) { this.assignedAgentId = assignedAgentId; }

    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }

    public int getReopenCount() { return reopenCount; }
    public void setReopenCount(int reopenCount) { this.reopenCount = reopenCount; }

    public boolean isSlaBreached() { return slaBreached; }
    public void setSlaBreached(boolean slaBreached) { this.slaBreached = slaBreached; }

    public boolean isAutoCloseReady() { return autoCloseReady; }
    public void setAutoCloseReady(boolean autoCloseReady) { this.autoCloseReady = autoCloseReady; }

    public List<TicketMessage> getMessages() { return messages; }
    public void setMessages(List<TicketMessage> messages) { this.messages = messages; }

    public TransientMap getTransientMap() { return this.transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SupportTicketActivityLog activityLog = new SupportTicketActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }
}
