package com.homebase.ecom.cms.dto.response;

import java.io.Serializable;
import java.util.Date;

public class ScheduleResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String pageId;
    private Date publishAt;
    private Date unpublishAt;
    private String timezone;
    private boolean isActive;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public Date getPublishAt() { return publishAt; }
    public void setPublishAt(Date publishAt) { this.publishAt = publishAt; }

    public Date getUnpublishAt() { return unpublishAt; }
    public void setUnpublishAt(Date unpublishAt) { this.unpublishAt = unpublishAt; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
