package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_media")
public class ProductMediaEntity {
    @Id
    private String id;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "is_primary")
    private boolean primary;

    @Column(name = "sort_order")
    private int sortOrder;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
