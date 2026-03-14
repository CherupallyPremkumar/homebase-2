package com.homebase.ecom.promo.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import org.chenile.workflow.model.*;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

@Entity
@Table(name = "coupon")
public class Coupon extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private java.util.UUID couponId;

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "promotion_id")
    private java.util.UUID promotionId;

    @Transient
    public TransientMap transientMap = new TransientMap();

    @Override
    public TransientMap getTransientMap() {
        return this.transientMap;
    }

    public void setTransientMap(TransientMap transientMap) {
        this.transientMap = transientMap;
    }

    @Column(name = "discount_type")
    private String discountType; // PERCENTAGE, FLAT

    @Column(name = "discount_value")
    private Double discountValue;

    @Column(name = "min_order_amount")
    private Double minOrderAmount;

    @Column(name = "max_discount_amount")
    private Double maxDiscountAmount;

    @Column(name = "expiry_date")
    private java.time.LocalDateTime expiryDate;

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "valid_product_ids")
    private List<String> validProductIds = new ArrayList<>();

    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", name = "valid_category_ids")
    private List<String> validCategoryIds = new ArrayList<>();

    @Column(name = "usage_count")
    private Integer currentUsage = 0;

    @Column(name = "max_usage")
    private Integer maxUsageCount;

    @Column(name = "single_use_per_customer")
    private boolean singleUsePerCustomer = false;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "promo_code")
    public List<PromoCodeActivityLog> activities = new ArrayList<>();

    // --- Business Logic Methods ---

    public boolean isValid() {
        return isActive() && !isExpired() && !isUsageLimitReached();
    }

    public boolean isActive() {
        return getCurrentState() != null && "ACTIVE".equalsIgnoreCase(getCurrentState().getStateId());
    }

    public boolean isExpired() {
        return expiryDate != null && expiryDate.isBefore(java.time.LocalDateTime.now());
    }

    public boolean isMinOrderMet(double cartTotal) {
        return minOrderAmount == null || cartTotal >= minOrderAmount;
    }

    public boolean isUsageLimitReached() {
        return maxUsageCount != null && currentUsage != null && currentUsage >= maxUsageCount;
    }

    public boolean canBeAppliedBy(java.util.UUID userId) {
        // This will likely need checking against a usage log repository in the service
        // layer,
        // but here we can check basic validity.
        return isValid();
    }

    public void incrementUsage() {
        if (currentUsage == null)
            currentUsage = 0;
        currentUsage++;
    }

    public boolean appliesTo(String productId, String categoryId) {
        // If no products or categories restricted, applies to everything
        if ((validProductIds == null || validProductIds.isEmpty()) &&
                (validCategoryIds == null || validCategoryIds.isEmpty())) {
            return true;
        }

        if (validProductIds != null && validProductIds.contains(productId)) {
            return true;
        }

        if (validCategoryIds != null && validCategoryIds.contains(categoryId)) {
            return true;
        }

        return false;
    }

    public double calculateDiscount(double amount) {
        double discount = 0;
        if ("PERCENTAGE".equalsIgnoreCase(discountType)) {
            discount = amount * (discountValue / 100.0);
        } else {
            discount = Math.min(amount, discountValue);
        }
        return discount;
    }

    // Getters and Setters
    public java.util.UUID getCouponId() {
        return couponId;
    }

    public void setCouponId(java.util.UUID couponId) {
        this.couponId = couponId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.util.UUID getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(java.util.UUID promotionId) {
        this.promotionId = promotionId;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public Double getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Double maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    public java.time.LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(java.time.LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public List<String> getValidProductIds() {
        return validProductIds;
    }

    public void setValidProductIds(List<String> validProductIds) {
        this.validProductIds = validProductIds;
    }

    public List<String> getValidCategoryIds() {
        return validCategoryIds;
    }

    public void setValidCategoryIds(List<String> validCategoryIds) {
        this.validCategoryIds = validCategoryIds;
    }

    public Integer getCurrentUsage() {
        return currentUsage;
    }

    public void setCurrentUsage(Integer currentUsage) {
        this.currentUsage = currentUsage;
    }

    public Integer getMaxUsageCount() {
        return maxUsageCount;
    }

    public void setMaxUsageCount(Integer maxUsageCount) {
        this.maxUsageCount = maxUsageCount;
    }

    public boolean isSingleUsePerCustomer() {
        return singleUsePerCustomer;
    }

    public void setSingleUsePerCustomer(boolean singleUsePerCustomer) {
        this.singleUsePerCustomer = singleUsePerCustomer;
    }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        PromoCodeActivityLog activityLog = new PromoCodeActivityLog();
        activityLog.setActivityName(eventId);
        activityLog.setActivityComment(comment);
        activityLog.setActivitySuccess(true);
        activities.add(activityLog);
        return activityLog;
    }
}
