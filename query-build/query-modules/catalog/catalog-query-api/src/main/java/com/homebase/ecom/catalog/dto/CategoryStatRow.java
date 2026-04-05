package com.homebase.ecom.catalog.dto;

import java.io.Serializable;

public class CategoryStatRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private String categoryId;
    private String categoryName;
    private boolean active;
    private long productCount;
    private long sellerCount;

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public long getProductCount() { return productCount; }
    public void setProductCount(long productCount) { this.productCount = productCount; }
    public long getSellerCount() { return sellerCount; }
    public void setSellerCount(long sellerCount) { this.sellerCount = sellerCount; }
}
