package com.homebase.ecom.cms.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "cms_block")
public class CmsBlockEntity extends BaseJpaEntity {

    @Column(name = "page_id", nullable = false)
    private String pageId;

    @Column(name = "block_type", length = 100)
    private String blockType;

    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    @Column(name = "sort_order")
    private int sortOrder;

    @Column(name = "active")
    private boolean active;

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
