package com.homebase.ecom.wishlist.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WishlistItemQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String productId;
    private String variantId;
    private Date addedAt;
    private BigDecimal priceWhenAdded;
    private boolean notifyOnPriceDrop;
    private String notes;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }
    public BigDecimal getPriceWhenAdded() { return priceWhenAdded; }
    public void setPriceWhenAdded(BigDecimal priceWhenAdded) { this.priceWhenAdded = priceWhenAdded; }
    public boolean isNotifyOnPriceDrop() { return notifyOnPriceDrop; }
    public void setNotifyOnPriceDrop(boolean notifyOnPriceDrop) { this.notifyOnPriceDrop = notifyOnPriceDrop; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
