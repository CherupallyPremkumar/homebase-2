package com.homebase.ecom.offer.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "offer")
public class OfferEntity extends AbstractJpaStateEntity {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "offer_type")
    private String offerType;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "original_price", precision = 19, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "offer_price", precision = 19, scale = 2)
    private BigDecimal offerPrice;

    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Column(name = "max_quantity")
    private int maxQuantity;

    @Column(name = "sold_quantity")
    private int soldQuantity;

    @Column(name = "seller_rating", precision = 3, scale = 2)
    private BigDecimal sellerRating;

    @Column(name = "buy_box_winner")
    private boolean buyBoxWinner;

    @Column(name = "fulfillment_type", length = 20)
    private String fulfillmentType;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    private BigDecimal shippingCost;

    @Column(name = "delivery_days")
    private Integer deliveryDays;

    // offer-003 columns
    @Column(name = "sku")
    private String sku;

    @Column(name = "variant_id")
    private String variantId;

    @Column(name = "mrp", precision = 12, scale = 2)
    private BigDecimal mrp;

    @Column(name = "stock_quantity")
    private int stockQuantity;

    @Column(name = "min_order_qty")
    private int minOrderQty;

    @Column(name = "max_order_qty")
    private int maxOrderQty;

    @Column(name = "item_condition", length = 50)
    private String itemCondition;

    @Column(name = "shipping_days_min")
    private Integer shippingDaysMin;

    @Column(name = "shipping_days_max")
    private Integer shippingDaysMax;

    @Column(name = "buy_box_score", precision = 5, scale = 2)
    private BigDecimal buyBoxScore;

    @Column(name = "active")
    private boolean active;

    @Column(name = "currency", length = 3)
    private String currency;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "offer_id")
    private List<OfferActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
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

    public List<OfferActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<OfferActivityLogEntity> activities) { this.activities = activities; }
}
