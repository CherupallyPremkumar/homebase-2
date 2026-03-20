package com.homebase.ecom.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * CatalogItemEntity - JPA Entity for Persistence
 */
@Entity
@Table(name = "catalog_items", indexes = {
        @Index(name = "idx_product_id", columnList = "product_id"),
        @Index(name = "idx_featured", columnList = "featured"),
        @Index(name = "idx_active", columnList = "active")
})
public class CatalogItemEntity extends BaseJpaEntity {

    @Column(name = "product_id", nullable = false, length = 255)
    private String productId;

    @Column(name = "featured")
    private Boolean featured = false;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "visibility_start_date")
    private LocalDateTime visibilityStartDate;

    @Column(name = "visibility_end_date")
    private LocalDateTime visibilityEndDate;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "in_stock")
    private Boolean inStock = true;

    @Column(name = "available_qty")
    private Integer availableQty = 0;

    @Column(name = "average_rating", precision = 3, scale = 2)
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @ElementCollection
    @CollectionTable(name = "catalog_item_tags", joinColumns = @JoinColumn(name = "catalog_item_id"))
    @Column(name = "tag", length = 50)
    private List<String> tags = new ArrayList<>();

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
}
