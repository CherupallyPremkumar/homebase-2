package com.homebase.ecom.cms.model;

import org.chenile.utils.entity.model.BaseEntity;

import java.time.LocalDateTime;

public class CmsPageVersion extends BaseEntity {
    private String pageId;
    private int versionNumber;
    private String blocksSnapshot;
    private String publishedBy;
    private LocalDateTime publishedAt;
    private String changeSummary;
    private String tenant;

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public int getVersionNumber() { return versionNumber; }
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }

    public String getBlocksSnapshot() { return blocksSnapshot; }
    public void setBlocksSnapshot(String blocksSnapshot) { this.blocksSnapshot = blocksSnapshot; }

    public String getPublishedBy() { return publishedBy; }
    public void setPublishedBy(String publishedBy) { this.publishedBy = publishedBy; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }

    public String getChangeSummary() { return changeSummary; }
    public void setChangeSummary(String changeSummary) { this.changeSummary = changeSummary; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
