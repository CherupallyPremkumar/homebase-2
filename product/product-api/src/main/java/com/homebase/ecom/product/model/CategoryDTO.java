package com.homebase.ecom.product.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryDTO implements Serializable {
    private String id;
    private String name;
    private String slug;
    private String parentId;
    private String path;
    private int depth;
    private int displayOrder;
    private boolean active;
    private List<String> attributeIds = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public int getDepth() { return depth; }
    public void setDepth(int depth) { this.depth = depth; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public List<String> getAttributeIds() { return attributeIds; }
    public void setAttributeIds(List<String> attributeIds) { this.attributeIds = attributeIds; }
}
