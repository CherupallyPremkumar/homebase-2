package com.homebase.ecom.cms.dto;

import java.io.Serializable;
import java.util.Date;

public class AnnouncementQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String announcementType;
    private String targetAudience;
    private String priority;
    private String status;
    private boolean dismissible;
    private Date scheduledDate;
    private Date expiryDate;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAnnouncementType() { return announcementType; }
    public void setAnnouncementType(String announcementType) { this.announcementType = announcementType; }
    public String getTargetAudience() { return targetAudience; }
    public void setTargetAudience(String targetAudience) { this.targetAudience = targetAudience; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isDismissible() { return dismissible; }
    public void setDismissible(boolean dismissible) { this.dismissible = dismissible; }
    public Date getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(Date scheduledDate) { this.scheduledDate = scheduledDate; }
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
