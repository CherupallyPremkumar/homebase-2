package com.homebase.ecom.onboarding.infrastructure.persistence.entity;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "supplier_onboarding")
public class OnboardingSagaEntity extends AbstractJpaStateEntity {

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "upi_id")
    private String upiId;

    @Column(name = "address")
    private String address;

    @Column(name = "commission_percentage")
    private Double commissionPercentage;

    @Column(name = "description")
    private String description;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "onboarding_id")
    private List<OnboardingSagaActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
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

    public List<OnboardingSagaActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<OnboardingSagaActivityLogEntity> activities) { this.activities = activities; }
}
