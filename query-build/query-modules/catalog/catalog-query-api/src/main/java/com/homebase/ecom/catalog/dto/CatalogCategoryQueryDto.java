package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CatalogCategoryQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String slug;
    private String imageUrl;
    private String bannerImageUrl;
    private boolean active;
    private boolean featured;
    private int level;
    private int productCount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getBannerImageUrl() { return bannerImageUrl; }
    public void setBannerImageUrl(String bannerImageUrl) { this.bannerImageUrl = bannerImageUrl; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isFeatured() { return featured; }
    public void setFeatured(boolean featured) { this.featured = featured; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getProductCount() { return productCount; }
    public void setProductCount(int productCount) { this.productCount = productCount; }
}
