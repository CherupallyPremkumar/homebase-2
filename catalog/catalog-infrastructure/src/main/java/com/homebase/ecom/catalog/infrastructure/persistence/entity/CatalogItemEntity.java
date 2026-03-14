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

    @Column(name = "product_id", nullable = false, length = 50)
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
}
