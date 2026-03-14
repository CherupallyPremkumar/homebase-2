package com.homebase.ecom.supplier.model;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import java.util.*;
import java.time.LocalDateTime;
import org.chenile.workflow.model.*;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;

public class Supplier extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity,
        ContainsTransientMap {

    private String name;
    private String userId;
    private String email;
    private String description;
    private String phone;
    private String upiId;
    private String address;
    private Double commissionPercentage;

    // Lifecycle tracking fields
    private LocalDateTime activeDate;
    private String rejectionReason;
    private String suspensionReason;
    private String blacklistReason;
    private LocalDateTime suspendedDate;
    private LocalDateTime blacklistedDate;
    private boolean productsDisabled;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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

    public TransientMap getTransientMap() { return this.transientMap; }
    public void setTransientMap(TransientMap transientMap) { this.transientMap = transientMap; }

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
}
