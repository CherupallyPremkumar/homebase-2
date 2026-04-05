package com.homebase.ecom.catalog.model;

import org.chenile.utils.entity.model.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * CatalogItem - Pure Domain Model (read model, NOT an STM entity).
 * Materialized view aggregating product, offer, inventory, and review data.
 */
public class CatalogItem extends BaseEntity {

    private String productId;
    private Boolean featured = false;
    private Integer displayOrder;
    private Boolean active = true;
    private LocalDateTime visibilityStartDate;
    private LocalDateTime visibilityEndDate;
    private String name;
    private BigDecimal price;
    private String description;
    private String brand;
    private String imageUrl;
    private Boolean inStock = true;
    private Integer availableQty = 0;
    private BigDecimal averageRating = BigDecimal.ZERO;
    private Integer reviewCount = 0;
    private List<String> tags = new ArrayList<>();
    private List<String> categoryIds = new ArrayList<>();
    private List<String> collectionIds = new ArrayList<>();
    private String tenant;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    public Integer getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(Integer availableQty) {
        this.availableQty = availableQty;
    }

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
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

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
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
