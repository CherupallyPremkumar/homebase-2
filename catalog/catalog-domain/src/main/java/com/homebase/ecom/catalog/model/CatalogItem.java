package com.homebase.ecom.catalog.model;

import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * CatalogItem - Pure Domain Model
 */
public class CatalogItem  extends AbstractExtendedStateEntity {

    private String productId;
    private Boolean featured = false;
    private Integer displayOrder;
    private Boolean active = true;
    private LocalDateTime visibilityStartDate;
    private LocalDateTime visibilityEndDate;
    private String name;
    private BigDecimal price;
    private List<String> tags = new ArrayList<>();
    private List<String> categoryIds = new ArrayList<>();
    private List<String> collectionIds = new ArrayList<>();

    // Getters and Setters


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getVisibilityStartDate() {
        return visibilityStartDate;
    }

    public void setVisibilityStartDate(LocalDateTime visibilityStartDate) {
        this.visibilityStartDate = visibilityStartDate;
    }

    public LocalDateTime getVisibilityEndDate() {
        return visibilityEndDate;
    }

    public void setVisibilityEndDate(LocalDateTime visibilityEndDate) {
        this.visibilityEndDate = visibilityEndDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getCollectionIds() {
        return collectionIds;
    }

    public void setCollectionIds(List<String> collectionIds) {
        this.collectionIds = collectionIds;
    }

    public boolean isCurrentlyVisible() {
        if (!active) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now();
        if (visibilityStartDate != null && now.isBefore(visibilityStartDate)) {
            return false;
        }
        if (visibilityEndDate != null && now.isAfter(visibilityEndDate)) {
            return false;
        }
        return true;
    }
}
