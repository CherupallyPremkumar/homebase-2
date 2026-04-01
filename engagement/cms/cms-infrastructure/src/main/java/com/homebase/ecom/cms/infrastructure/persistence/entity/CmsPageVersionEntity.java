package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "cms_page_version")
public class CmsPageVersionEntity extends BaseJpaEntity {

    @Column(name = "page_id", nullable = false)
    private String pageId;

    @Column(name = "version_number", nullable = false)
    private int versionNumber;

    @Column(name = "blocks_snapshot", columnDefinition = "TEXT")
    private String blocksSnapshot;

    @Column(name = "published_by")
    private String publishedBy;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "change_summary", length = 1000)
    private String changeSummary;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getBlocksSnapshot() {
        return blocksSnapshot;
    }

    public void setBlocksSnapshot(String blocksSnapshot) {
        this.blocksSnapshot = blocksSnapshot;
    }

    public String getPublishedBy() {
        return publishedBy;
    }

    public void setPublishedBy(String publishedBy) {
        this.publishedBy = publishedBy;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChangeSummary() {
        return changeSummary;
    }

    public void setChangeSummary(String changeSummary) {
        this.changeSummary = changeSummary;
    }
}
