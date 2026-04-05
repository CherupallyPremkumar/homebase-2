package com.homebase.ecom.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class RefundQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String paymentId;
    private BigDecimal amount;
    private String reason;
    private String status;
    private Date createdTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
}
