package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;

public class StorefrontCategoryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String slug;
    private String parentId;
    private String imageUrl;
    private String description;
    private int level;
    private int sortOrder;
    private long productCount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getParentId() { return parentId; }
    public void setParentId(String parentId) { this.parentId = parentId; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
    public long getProductCount() { return productCount; }
    public void setProductCount(long productCount) { this.productCount = productCount; }
}
