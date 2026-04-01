package com.homebase.ecom.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for Supplier query responses (legacy package).
 */
public class SupplierDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private String businessName;
    private String businessType;
    private String contactEmail;
    private double rating;
    private double fulfillmentRate;
    private int totalOrders;
    private double commissionRate;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public double getFulfillmentRate() { return fulfillmentRate; }
    public void setFulfillmentRate(double fulfillmentRate) { this.fulfillmentRate = fulfillmentRate; }
    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public double getCommissionRate() { return commissionRate; }
    public void setCommissionRate(double commissionRate) { this.commissionRate = commissionRate; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
