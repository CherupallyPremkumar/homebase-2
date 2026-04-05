package com.homebase.ecom.cms.dto.response;

import java.io.Serializable;
import java.util.Date;

public class PageVersionResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String pageId;
    private int versionNumber;
    private String publishedBy;
    private Date publishedAt;
    private String changeSummary;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }

    public String getPublishedBy() { return publishedBy; }
    public void setPublishedBy(String publishedBy) { this.publishedBy = publishedBy; }

    public Date getPublishedAt() { return publishedAt; }
    public void setPublishedAt(Date publishedAt) { this.publishedAt = publishedAt; }

    public String getChangeSummary() { return changeSummary; }
    public void setChangeSummary(String changeSummary) { this.changeSummary = changeSummary; }
}
