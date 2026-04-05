package com.homebase.ecom.oms.dto;

import java.io.Serializable;
import java.util.Date;

public class OrderStatusTimelineQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String activityId;
    private String orderId;
    private String activityName;
    private String comment;
    private boolean success;
    private String performedBy;
    private Date activityTime;

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public boolean getSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
    public Date getActivityTime() { return activityTime; }
    public void setActivityTime(Date activityTime) { this.activityTime = activityTime; }
}
