package com.homebase.ecom.dto;

import java.io.Serializable;
import java.util.Date;
import org.chenile.stm.State;
import org.chenile.stm.StateEntity;

public class CartDto implements Serializable, StateEntity {
    private static final long serialVersionUID = 1L;

    private String id;
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
    private Date createdTime;
    private Date lastModifiedTime;
    private String lastModifiedBy;
    private String createdBy;
    private String tenant;
    private Long version;

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

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
