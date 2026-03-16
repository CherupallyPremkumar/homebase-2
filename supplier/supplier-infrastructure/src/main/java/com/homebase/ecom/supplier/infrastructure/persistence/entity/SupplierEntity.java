package com.homebase.ecom.supplier.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JPA Entity for Supplier.
 * Maps to supplier table.
 * Extends AbstractJpaStateEntity for Chenile STM integration.
 */
@Entity
@Table(name = "supplier")
public class SupplierEntity extends AbstractJpaStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    // --- Identity & Business Info ---

    @Column(name = "user_id")
    private String userId;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "business_type")
    private String businessType;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "bank_account_id")
    private String bankAccountId;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(length = 2000)
    private String address;

    // --- Performance Metrics ---

    @Column(name = "rating")
    private double rating;

    @Column(name = "total_orders")
    private int totalOrders;

    @Column(name = "total_returns")
    private int totalReturns;

    @Column(name = "fulfillment_rate")
    private double fulfillmentRate;

    @Column(name = "avg_shipping_days")
    private double avgShippingDays;

    @Column(name = "commission_rate")
    private double commissionRate;

    // --- Lifecycle Tracking ---

    @Column(name = "active_date")
    private LocalDateTime activeDate;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "suspension_reason", length = 1000)
    private String suspensionReason;

    @Column(name = "termination_reason", length = 1000)
    private String terminationReason;

    @Column(name = "probation_reason", length = 1000)
    private String probationReason;

    @Column(name = "suspended_date")
    private LocalDateTime suspendedDate;

    @Column(name = "terminated_date")
    private LocalDateTime terminatedDate;

    @Column(name = "probation_date")
    private LocalDateTime probationDate;

    @Column(name = "products_disabled")
    private boolean productsDisabled;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "supplier_id")
    private List<SupplierActivityLogEntity> activities = new ArrayList<>();

    @Transient
    private TransientMap transientMap = new TransientMap();

    // --- ActivityEnabledStateEntity ---

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SupplierActivityLogEntity log = new SupplierActivityLogEntity();
        log.setName(eventId);
        log.setComment(comment);
        log.setSuccess(true);
        activities.add(log);
        return log;
    }

    // --- ContainsTransientMap ---

    @Override
    public TransientMap getTransientMap() { return transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

    // --- Getters / Setters ---

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

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getTotalReturns() { return totalReturns; }
    public void setTotalReturns(int totalReturns) { this.totalReturns = totalReturns; }

    public double getFulfillmentRate() { return fulfillmentRate; }
    public void setFulfillmentRate(double fulfillmentRate) { this.fulfillmentRate = fulfillmentRate; }

    public double getAvgShippingDays() { return avgShippingDays; }
    public void setAvgShippingDays(double avgShippingDays) { this.avgShippingDays = avgShippingDays; }

    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

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

    public List<SupplierActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<SupplierActivityLogEntity> activities) { this.activities = activities; }
}
