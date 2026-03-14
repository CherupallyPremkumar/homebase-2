package com.homebase.ecom.returnrequest.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ReturnRequestDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String orderId;
    private BigDecimal amount;
    private String reason;
    private String status;
    private String stateId;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
