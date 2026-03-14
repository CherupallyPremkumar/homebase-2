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

public class SupportTicket extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String userId;
    private String orderId;
    private String subject;
    private String category;
    private String priority = "MEDIUM";
    private String assignedTo;
    private Date resolvedAt;
    private String description;

    private List<TicketMessage> messages = new ArrayList<>();

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getAssignedTo() { return assignedTo; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

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
