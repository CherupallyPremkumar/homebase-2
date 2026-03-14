package com.homebase.ecom.catalog.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * CategoryProductMapping - Pure Domain Model
 */
public class CategoryProductMapping extends BaseEntity {

    private String categoryId;
    private String productId;
    private Integer displayOrder;
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
}
