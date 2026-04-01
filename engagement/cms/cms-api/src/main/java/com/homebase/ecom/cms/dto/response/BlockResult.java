package com.homebase.ecom.cms.dto.response;

import java.io.Serializable;

public class BlockResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String pageId;
    private String blockType;
    private String data;
    private int sortOrder;
    private boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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
}
