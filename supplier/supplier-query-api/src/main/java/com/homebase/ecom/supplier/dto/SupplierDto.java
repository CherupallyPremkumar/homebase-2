package com.homebase.ecom.supplier.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Supplier query responses.
 * Used by MyBatis queries in supplier-query module.
 */
public class SupplierDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String businessName;
    private String businessType;
    private String taxId;
    private String bankAccountId;
    private String contactEmail;
    private String contactPhone;
    private String address;
    private double rating;
    private double fulfillmentRate;
    private int totalOrders;
    private int totalReturns;
    private double avgShippingDays;
    private double commissionRate;
    private String stateId;
    private Date createdAt;
    private Date activeDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

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

    public double getFulfillmentRate() { return fulfillmentRate; }
    public void setFulfillmentRate(double fulfillmentRate) { this.fulfillmentRate = fulfillmentRate; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getTotalReturns() { return totalReturns; }
    public void setTotalReturns(int totalReturns) { this.totalReturns = totalReturns; }

    public double getAvgShippingDays() { return avgShippingDays; }
    public void setAvgShippingDays(double avgShippingDays) { this.avgShippingDays = avgShippingDays; }

    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getActiveDate() { return activeDate; }
    public void setActiveDate(Date activeDate) { this.activeDate = activeDate; }
}
