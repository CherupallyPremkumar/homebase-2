package com.homebase.ecom.cms.model;

import org.chenile.utils.entity.model.BaseEntity;

public class CmsBlock extends BaseEntity {
    private String pageId;
    private String blockType;
    private String data;
    private int sortOrder;
    private boolean active;
    private String tenant;

    public String getPageId() { return pageId; }
    public void setPageId(String pageId) { this.pageId = pageId; }

    public String getBlockType() { return blockType; }
    public void setBlockType(String blockType) { this.blockType = blockType; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
