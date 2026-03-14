package com.homebase.ecom.product.model;

import java.io.Serializable;

public class ProductMediaDTO implements Serializable {
    private String id;
    private String productId;
    private String assetId;
    private boolean primary;
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
