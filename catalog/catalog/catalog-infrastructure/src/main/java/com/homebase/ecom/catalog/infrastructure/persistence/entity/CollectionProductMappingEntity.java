package com.homebase.ecom.catalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

/**
 * CollectionProductMappingEntity - JPA Entity for Persistence
 */
@Entity
@Table(name = "collection_product_mapping", indexes = {
        @Index(name = "idx_colpm_collection_id", columnList = "collection_id"),
        @Index(name = "idx_colpm_product_id", columnList = "product_id")
})
public class CollectionProductMappingEntity extends BaseJpaEntity {

    @Column(name = "collection_id", nullable = false, length = 255)
    private String collectionId;

    @Column(name = "product_id", nullable = false, length = 255)
    private String productId;

    @Column(name = "display_order")
    private Integer displayOrder;

    @Column(name = "added_by", length = 50)
    private String addedBy;

    // Getters and Setters

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

    @Override
    protected String getPrefix() {
        return "CPM";
    }
}
