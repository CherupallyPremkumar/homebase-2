package com.homebase.ecom.onboarding.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SupplierOnboardingRequest {
    @NotBlank(message = "Supplier name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String description;

    @NotBlank(message = "UPI ID is required for settlements")
    private String upiId;

    private String phone;
    private String address;
    private Double commissionPercentage;
    private java.util.Map<String, Object> attributes;

    public SupplierOnboardingRequest() {
    }

    public Double getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpiId() {
        return upiId;
    }

    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public java.util.Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(java.util.Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "SupplierOnboardingRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", upiId='" + upiId + '\'' +
                '}';
    }
}
