package com.homebase.ecom.onboarding.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

public class OnboardingSaga extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String supplierId;
    private String supplierName;
    private String email;
    private String phone;
    private String upiId;
    private String address;
    private Double commissionPercentage;
    private String description;
    private String errorMessage;
    private int retryCount;

    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getCommissionPercentage() { return commissionPercentage; }
    public void setCommissionPercentage(Double commissionPercentage) { this.commissionPercentage = commissionPercentage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        OnboardingSagaActivityLog activityLog = new OnboardingSagaActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }
}
