package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "cms_schedule")
public class CmsScheduleEntity extends BaseJpaEntity {

    @Column(name = "page_id", nullable = false)
    private String pageId;

    @Column(name = "publish_at")
    private LocalDateTime publishAt;

    @Column(name = "unpublish_at")
    private LocalDateTime unpublishAt;

    @Column(name = "timezone", length = 50)
    private String timezone;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "executed")
    private boolean executed;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public LocalDateTime getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(LocalDateTime publishAt) {
        this.publishAt = publishAt;
    }

    public LocalDateTime getUnpublishAt() {
        return unpublishAt;
    }

    public void setUnpublishAt(LocalDateTime unpublishAt) {
        this.unpublishAt = unpublishAt;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isExecuted() {
        return executed;
    }

    public void setExecuted(boolean executed) {
        this.executed = executed;
    }
}
