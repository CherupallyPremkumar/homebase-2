package com.homebase.ecom.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

/**
 * CategoryProductMappingEntity - JPA Entity for Persistence
 */
@Entity
@Table(name = "category_product_mapping", indexes = {
        @Index(name = "idx_category_id", columnList = "category_id"),
        @Index(name = "idx_product_id", columnList = "product_id")
})
public class CategoryProductMappingEntity extends BaseJpaEntity {

    @Column(name = "category_id", nullable = false, length = 100)
    private String categoryId;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "added_by", length = 50)
    private String addedBy;

    // Getters and Setters

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    @Override
    protected String getPrefix() {
        return "CATPM";
    }
}
