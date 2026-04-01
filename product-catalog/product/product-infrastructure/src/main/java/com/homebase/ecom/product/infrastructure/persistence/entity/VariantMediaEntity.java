package com.homebase.ecom.product.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "variant_media")
@IdClass(VariantMediaId.class)
public class VariantMediaEntity {
    @Id
    @Column(name = "variant_id")
    private String variantId;

    @Id
    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "is_primary")
    private boolean primary;

    @Column(name = "sort_order")
    private int sortOrder;

    // Getters and Setters
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getAssetId() { return assetId; }
    public void setAssetId(String assetId) { this.assetId = assetId; }

    public boolean isPrimary() { return primary; }
    public void setPrimary(boolean primary) { this.primary = primary; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
