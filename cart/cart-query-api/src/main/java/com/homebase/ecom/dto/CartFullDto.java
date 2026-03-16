package com.homebase.ecom.dto;

import java.io.Serializable;
import java.util.Date;
import org.chenile.stm.State;
import org.chenile.stm.StateEntity;

/**
 * Full denormalized cart view: cart + items + activity log joined.
 * Each row is one (item, activity) combination. Use this query
 * with cartId filter to get everything about a cart in one call.
 */
public class CartFullDto implements Serializable, StateEntity {
    private static final long serialVersionUID = 1L;

    // Cart fields
    private String cartId;
    private String userId;
    private String sessionId;
    private long subtotal;
    private String currency;
    private String couponCodes;
    private long discountAmount;
    private long total;
    private Date expiresAt;
    private String notes;
    private String description;
    private String stateId;
    private String flowId;
    private Date cartCreatedTime;
    private Date cartLastModifiedTime;
    private String lastModifiedBy;
    private String createdBy;
    private String tenant;
    private Long version;
    private Date stateEntryTime;
    private Date slaYellowDate;
    private Date slaRedDate;
    private Integer slaTendingLate;
    private Integer slaLate;

    // Item fields
    private String itemId;
    private String productId;
    private String variantId;
    private String sku;
    private String productName;
    private String supplierId;
    private Integer quantity;
    private Long unitPrice;
    private Long lineTotal;
    private Date itemCreatedTime;
    private Date itemLastModifiedTime;

    // Activity fields
    private String activityId;
    private String activityName;
    private Boolean activitySuccess;
    private String activityComment;
    private Date activityCreatedTime;

    @Override
    public State getCurrentState() {
        return new State(stateId, flowId);
    }

    @Override
    public void setCurrentState(State state) {
        if (state != null) {
            this.stateId = state.getStateId();
            this.flowId = state.getFlowId();
        }
    }

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public long getSubtotal() { return subtotal; }
    public void setSubtotal(long subtotal) { this.subtotal = subtotal; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getCouponCodes() { return couponCodes; }
    public void setCouponCodes(String couponCodes) { this.couponCodes = couponCodes; }
    public long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(long discountAmount) { this.discountAmount = discountAmount; }
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    public Date getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }
    public Date getCartCreatedTime() { return cartCreatedTime; }
    public void setCartCreatedTime(Date cartCreatedTime) { this.cartCreatedTime = cartCreatedTime; }
    public Date getCartLastModifiedTime() { return cartLastModifiedTime; }
    public void setCartLastModifiedTime(Date cartLastModifiedTime) { this.cartLastModifiedTime = cartLastModifiedTime; }
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public Date getStateEntryTime() { return stateEntryTime; }
    public void setStateEntryTime(Date stateEntryTime) { this.stateEntryTime = stateEntryTime; }
    public Date getSlaYellowDate() { return slaYellowDate; }
    public void setSlaYellowDate(Date slaYellowDate) { this.slaYellowDate = slaYellowDate; }
    public Date getSlaRedDate() { return slaRedDate; }
    public void setSlaRedDate(Date slaRedDate) { this.slaRedDate = slaRedDate; }
    public Integer getSlaTendingLate() { return slaTendingLate; }
    public void setSlaTendingLate(Integer slaTendingLate) { this.slaTendingLate = slaTendingLate; }
    public Integer getSlaLate() { return slaLate; }
    public void setSlaLate(Integer slaLate) { this.slaLate = slaLate; }
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public Long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Long unitPrice) { this.unitPrice = unitPrice; }
    public Long getLineTotal() { return lineTotal; }
    public void setLineTotal(Long lineTotal) { this.lineTotal = lineTotal; }
    public Date getItemCreatedTime() { return itemCreatedTime; }
    public void setItemCreatedTime(Date itemCreatedTime) { this.itemCreatedTime = itemCreatedTime; }
    public Date getItemLastModifiedTime() { return itemLastModifiedTime; }
    public void setItemLastModifiedTime(Date itemLastModifiedTime) { this.itemLastModifiedTime = itemLastModifiedTime; }
    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public Boolean getActivitySuccess() { return activitySuccess; }
    public void setActivitySuccess(Boolean activitySuccess) { this.activitySuccess = activitySuccess; }
    public String getActivityComment() { return activityComment; }
    public void setActivityComment(String activityComment) { this.activityComment = activityComment; }
    public Date getActivityCreatedTime() { return activityCreatedTime; }
    public void setActivityCreatedTime(Date activityCreatedTime) { this.activityCreatedTime = activityCreatedTime; }
}
