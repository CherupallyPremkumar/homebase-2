package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String userId;
    private String customerEmail;
    private String status;
    private String stateId;
    private BigDecimal totalAmount;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
