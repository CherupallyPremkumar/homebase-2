package com.homebase.ecom.supplier.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import java.time.Instant;
import java.time.LocalDateTime;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

/**
 * Domain model for Supplier bounded context.
 * Fields aligned with DDD supplier aggregate: identity, business info,
 * performance metrics, and lifecycle tracking.
 */
public class Supplier extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    // --- Identity & Business Info ---
    private String userId;
    private String businessName;
    private String businessType; // INDIVIDUAL or COMPANY
    private String taxId;
    private String bankAccountId;
    private String contactEmail;
    private String contactPhone;
    private String address;

    // --- Performance Metrics ---
    private Double rating;
    private Integer totalOrders;
    private Integer totalReturns;
    private Double fulfillmentRate;
    private Double avgShippingDays;
    private Double commissionRate;

    // --- Lifecycle Tracking ---
    private LocalDateTime activeDate;
    private String rejectionReason;
    private String suspensionReason;
    private String terminationReason;
    private String probationReason;
    private LocalDateTime suspendedDate;
    private LocalDateTime terminatedDate;
    private LocalDateTime probationDate;
    private boolean productsDisabled;
    private Instant deletedAt;
    private String tenant;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // --- Identity & Business Info Accessors ---

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    public String getBankAccountId() { return bankAccountId; }
    public void setBankAccountId(String bankAccountId) { this.bankAccountId = bankAccountId; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // --- Performance Metrics Accessors ---

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public Integer getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Integer totalOrders) { this.totalOrders = totalOrders; }

    public Integer getTotalReturns() { return totalReturns; }
    public void setTotalReturns(Integer totalReturns) { this.totalReturns = totalReturns; }

    public Double getFulfillmentRate() { return fulfillmentRate; }
    public void setFulfillmentRate(Double fulfillmentRate) { this.fulfillmentRate = fulfillmentRate; }

    public Double getAvgShippingDays() { return avgShippingDays; }
    public void setAvgShippingDays(Double avgShippingDays) { this.avgShippingDays = avgShippingDays; }

    public Double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(Double commissionRate) { this.commissionRate = commissionRate; }

    // --- Lifecycle Tracking Accessors ---

    public LocalDateTime getActiveDate() { return activeDate; }
    public void setActiveDate(LocalDateTime activeDate) { this.activeDate = activeDate; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getSuspensionReason() { return suspensionReason; }
    public void setSuspensionReason(String suspensionReason) { this.suspensionReason = suspensionReason; }

    public String getTerminationReason() { return terminationReason; }
    public void setTerminationReason(String terminationReason) { this.terminationReason = terminationReason; }

    public String getProbationReason() { return probationReason; }
    public void setProbationReason(String probationReason) { this.probationReason = probationReason; }

    public LocalDateTime getSuspendedDate() { return suspendedDate; }
    public void setSuspendedDate(LocalDateTime suspendedDate) { this.suspendedDate = suspendedDate; }

    public LocalDateTime getTerminatedDate() { return terminatedDate; }
    public void setTerminatedDate(LocalDateTime terminatedDate) { this.terminatedDate = terminatedDate; }

    public LocalDateTime getProbationDate() { return probationDate; }
    public void setProbationDate(LocalDateTime probationDate) { this.probationDate = probationDate; }

    public boolean isProductsDisabled() { return productsDisabled; }
    public void setProductsDisabled(boolean productsDisabled) { this.productsDisabled = productsDisabled; }

    public Instant getDeletedAt() { return deletedAt; }
    public void setDeletedAt(Instant deletedAt) { this.deletedAt = deletedAt; }

    // --- Backward compatibility: name maps to businessName ---

    public String getName() { return businessName; }
    public void setName(String name) { this.businessName = name; }

    // --- TransientMap ---

    public TransientMap getTransientMap() { return this.transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    // --- Activities ---

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SupplierActivityLog activityLog = new SupplierActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        activities.add(activityLog);
        return activityLog;
    }

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
