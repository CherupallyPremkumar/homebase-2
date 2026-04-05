package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CategoryTreeRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String slug;
    private String description;
    private String parentId;
    private int level;
    private int displayOrder;
    private boolean active;
    private boolean featured;
    private String imageUrl;
    private String icon;
    private boolean showInMenu;
    private boolean hasChildren;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public boolean isShowInMenu() { return showInMenu; }
    public void setShowInMenu(boolean showInMenu) { this.showInMenu = showInMenu; }
    public boolean isHasChildren() { return hasChildren; }
    public void setHasChildren(boolean hasChildren) { this.hasChildren = hasChildren; }
}
