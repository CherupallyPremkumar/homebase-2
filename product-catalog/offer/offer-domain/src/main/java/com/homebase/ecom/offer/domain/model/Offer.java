package com.homebase.ecom.offer.domain.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Offer domain model. Represents a seller's offer on a product.
 * Maps to the 'offer' table with all columns from offer-001 and offer-003 migrations.
 */
public class Offer extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String productId;
    private String supplierId;
    private OfferType offerType;
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
    private String tenant;

    // offer-003 columns
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

    private TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // ── Getters / Setters ──────────────────────────────────────────────────

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public OfferType getOfferType() { return offerType; }
    public void setOfferType(OfferType offerType) { this.offerType = offerType; }

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

    // ── STM / Activity support ─────────────────────────────────────────────

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() { return activities; }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        OfferActivityLog log = new OfferActivityLog();
        log.activityName = eventId;
        log.activityComment = comment;
        log.activitySuccess = true;
        this.activities.add(log);
        return log;
    }

    // ── Domain logic ───────────────────────────────────────────────────────

    /**
     * Is the offer currently live and available for purchase?
     */
    public boolean isLive() {
        return getCurrentState() != null && "LIVE".equals(getCurrentState().getStateId());
    }

    /**
     * Has the offer passed its end date?
     */
    public boolean isExpired() {
        return endDate != null && new Date().after(endDate);
    }

    /**
     * Compute margin percent: (originalPrice - offerPrice) / originalPrice * 100.
     */
    public BigDecimal computeMarginPercent() {
        if (originalPrice == null || offerPrice == null || originalPrice.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return originalPrice.subtract(offerPrice)
                .divide(originalPrice, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
