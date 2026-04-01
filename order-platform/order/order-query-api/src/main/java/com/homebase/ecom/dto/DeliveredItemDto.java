package com.homebase.ecom.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DeliveredItemDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String orderItemId;
    private String productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal itemSalesAmount;
    private Date orderDate;

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getItemSalesAmount() { return itemSalesAmount; }
    public void setItemSalesAmount(BigDecimal itemSalesAmount) { this.itemSalesAmount = itemSalesAmount; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
