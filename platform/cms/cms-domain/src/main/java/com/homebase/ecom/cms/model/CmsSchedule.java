package com.homebase.ecom.cms.model;

import org.chenile.utils.entity.model.BaseEntity;

import java.time.LocalDateTime;

public class CmsSchedule extends BaseEntity {
    private String pageId;
    private LocalDateTime publishAt;
    private LocalDateTime unpublishAt;
    private String timezone;
    private boolean isActive;
    private boolean executed;
    private String tenant;

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public LocalDateTime getPublishAt() { return publishAt; }
    public void setPublishAt(LocalDateTime publishAt) { this.publishAt = publishAt; }

    public LocalDateTime getUnpublishAt() { return unpublishAt; }
    public void setUnpublishAt(LocalDateTime unpublishAt) { this.unpublishAt = unpublishAt; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public boolean isExecuted() { return executed; }
    public void setExecuted(boolean executed) { this.executed = executed; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
