package com.homebase.ecom.offer.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OfferDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String productId;
    private String supplierId;
    private String offerType;
    private String title;
    private String description;
    private BigDecimal originalPrice;
    private BigDecimal offerPrice;
    private BigDecimal discountPercent;
    private Date startDate;
    private Date endDate;
    private int maxQuantity;
    private int soldQuantity;
    private BigDecimal sellerRating;
    private boolean buyBoxWinner;
    private String fulfillmentType;
    private BigDecimal shippingCost;
    private Integer deliveryDays;
    private String status;

    // offer-003 fields
    private String sku;
    private String variantId;
    private BigDecimal mrp;
    private int stockQuantity;
    private int minOrderQty;
    private int maxOrderQty;
    private String itemCondition;
    private Integer shippingDaysMin;
    private Integer shippingDaysMax;
    private BigDecimal buyBoxScore;
    private boolean active;
    private String currency;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public int getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(int maxQuantity) { this.maxQuantity = maxQuantity; }

    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }

    public BigDecimal getSellerRating() { return sellerRating; }
    public void setSellerRating(BigDecimal sellerRating) { this.sellerRating = sellerRating; }

    public boolean isBuyBoxWinner() { return buyBoxWinner; }
    public void setBuyBoxWinner(boolean buyBoxWinner) { this.buyBoxWinner = buyBoxWinner; }

    public String getFulfillmentType() { return fulfillmentType; }
    public void setFulfillmentType(String fulfillmentType) { this.fulfillmentType = fulfillmentType; }

    public BigDecimal getShippingCost() { return shippingCost; }
    public void setShippingCost(BigDecimal shippingCost) { this.shippingCost = shippingCost; }

    public Integer getDeliveryDays() { return deliveryDays; }
    public void setDeliveryDays(Integer deliveryDays) { this.deliveryDays = deliveryDays; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public BigDecimal getMrp() { return mrp; }
    public void setMrp(BigDecimal mrp) { this.mrp = mrp; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getMinOrderQty() { return minOrderQty; }
    public void setMinOrderQty(int minOrderQty) { this.minOrderQty = minOrderQty; }

    public int getMaxOrderQty() { return maxOrderQty; }
    public void setMaxOrderQty(int maxOrderQty) { this.maxOrderQty = maxOrderQty; }

    public String getItemCondition() { return itemCondition; }
    public void setItemCondition(String itemCondition) { this.itemCondition = itemCondition; }

    public Integer getShippingDaysMin() { return shippingDaysMin; }
    public void setShippingDaysMin(Integer shippingDaysMin) { this.shippingDaysMin = shippingDaysMin; }

    public Integer getShippingDaysMax() { return shippingDaysMax; }
    public void setShippingDaysMax(Integer shippingDaysMax) { this.shippingDaysMax = shippingDaysMax; }

    public BigDecimal getBuyBoxScore() { return buyBoxScore; }
    public void setBuyBoxScore(BigDecimal buyBoxScore) { this.buyBoxScore = buyBoxScore; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
