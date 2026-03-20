package com.homebase.ecom.catalog.model;

import org.chenile.utils.entity.model.BaseEntity;

/**
 * CollectionProductMapping - Pure Domain Model
 */
public class CollectionProductMapping  extends BaseEntity {

    private String collectionId;
    private String productId;
    private Integer displayOrder;
    private String addedBy;
    private String tenant;

    public String getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
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

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
