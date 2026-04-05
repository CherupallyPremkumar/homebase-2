package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class WishlistItemQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String productId;
    private String variantId;
    private Date addedAt;
    private BigDecimal priceWhenAdded;
    private boolean notifyOnPriceDrop;
    private String productName;
    private String brand;
    private String imageUrl;
    private BigDecimal currentPrice;
    private BigDecimal mrp;
    private BigDecimal offerPrice;
    private boolean inStock;
    private BigDecimal discount;
    private BigDecimal averageRating;
    private int reviewCount;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public Date getAddedAt() { return addedAt; }
    public void setAddedAt(Date addedAt) { this.addedAt = addedAt; }
    public BigDecimal getPriceWhenAdded() { return priceWhenAdded; }
    public void setPriceWhenAdded(BigDecimal priceWhenAdded) { this.priceWhenAdded = priceWhenAdded; }
    public boolean getNotifyOnPriceDrop() { return notifyOnPriceDrop; }
    public void setNotifyOnPriceDrop(boolean notifyOnPriceDrop) { this.notifyOnPriceDrop = notifyOnPriceDrop; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }
    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }
    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }
    public boolean getInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getAverageRating() { return averageRating; }
    public void setAverageRating(BigDecimal averageRating) { this.averageRating = averageRating; }
    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
}
