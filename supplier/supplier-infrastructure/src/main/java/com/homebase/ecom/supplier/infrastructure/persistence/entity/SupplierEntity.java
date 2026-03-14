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

    @Column(nullable = false)
    private String name;

    @Column(name = "user_id")
    private String userId;

    private String email;

    @Column(length = 1000)
    private String description;

    @Column(name = "phone_number")
    private String phone;

    @Column(name = "upi_id")
    private String upiId;

    @Column(length = 2000)
    private String address;

    @Column(name = "commission_percentage")
    private Double commissionPercentage;

    // Lifecycle tracking fields
    @Column(name = "active_date")
    private LocalDateTime activeDate;

    @Column(name = "rejection_reason", length = 1000)
    private String rejectionReason;

    @Column(name = "suspension_reason", length = 1000)
    private String suspensionReason;

    @Column(name = "blacklist_reason", length = 1000)
    private String blacklistReason;

    @Column(name = "suspended_date")
    private LocalDateTime suspendedDate;

    @Column(name = "blacklisted_date")
    private LocalDateTime blacklistedDate;

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

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getCommissionPercentage() { return commissionPercentage; }
    public void setCommissionPercentage(Double commissionPercentage) { this.commissionPercentage = commissionPercentage; }

    public LocalDateTime getActiveDate() { return activeDate; }
    public void setActiveDate(LocalDateTime activeDate) { this.activeDate = activeDate; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getSuspensionReason() { return suspensionReason; }
    public void setSuspensionReason(String suspensionReason) { this.suspensionReason = suspensionReason; }

    public String getBlacklistReason() { return blacklistReason; }
    public void setBlacklistReason(String blacklistReason) { this.blacklistReason = blacklistReason; }

    public LocalDateTime getSuspendedDate() { return suspendedDate; }
    public void setSuspendedDate(LocalDateTime suspendedDate) { this.suspendedDate = suspendedDate; }

    public LocalDateTime getBlacklistedDate() { return blacklistedDate; }
    public void setBlacklistedDate(LocalDateTime blacklistedDate) { this.blacklistedDate = blacklistedDate; }

    public boolean isProductsDisabled() { return productsDisabled; }
    public void setProductsDisabled(boolean productsDisabled) { this.productsDisabled = productsDisabled; }

    public List<SupplierActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<SupplierActivityLogEntity> activities) { this.activities = activities; }
}
