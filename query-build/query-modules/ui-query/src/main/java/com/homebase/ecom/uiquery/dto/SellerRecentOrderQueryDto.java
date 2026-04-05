package com.homebase.ecom.uiquery.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SellerRecentOrderQueryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String orderState;
    private BigDecimal totalAmount;
    private String currency;
    private Date createdTime;
    private int itemCount;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderState() { return orderState; }
    public void setOrderState(String orderState) { this.orderState = orderState; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}
