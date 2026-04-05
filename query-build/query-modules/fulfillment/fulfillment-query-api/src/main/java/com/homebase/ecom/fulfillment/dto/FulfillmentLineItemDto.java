package com.homebase.ecom.fulfillment.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class FulfillmentLineItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String lineItemId;
    private String fulfillmentOrderId;
    private String orderItemId;
    private String productId;
    private String variantId;
    private String sku;
    private int quantityOrdered;
    private int quantityAllocated;
    private int quantityPicked;
    private int quantityPacked;
    private int quantityShipped;
    private int quantityShort;
    private String binLocation;
    private String itemStatus;
    private String productName;
    private String brand;
    private int weightGrams;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public String getLineItemId() { return lineItemId; }
    public void setLineItemId(String lineItemId) { this.lineItemId = lineItemId; }
    public String getFulfillmentOrderId() { return fulfillmentOrderId; }
    public void setFulfillmentOrderId(String fulfillmentOrderId) { this.fulfillmentOrderId = fulfillmentOrderId; }
    public String getOrderItemId() { return orderItemId; }
    public void setOrderItemId(String orderItemId) { this.orderItemId = orderItemId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantityOrdered() { return quantityOrdered; }
    public void setQuantityOrdered(int quantityOrdered) { this.quantityOrdered = quantityOrdered; }
    public int getQuantityAllocated() { return quantityAllocated; }
    public void setQuantityAllocated(int quantityAllocated) { this.quantityAllocated = quantityAllocated; }
    public int getQuantityPicked() { return quantityPicked; }
    public void setQuantityPicked(int quantityPicked) { this.quantityPicked = quantityPicked; }
    public int getQuantityPacked() { return quantityPacked; }
    public void setQuantityPacked(int quantityPacked) { this.quantityPacked = quantityPacked; }
    public int getQuantityShipped() { return quantityShipped; }
    public void setQuantityShipped(int quantityShipped) { this.quantityShipped = quantityShipped; }
    public int getQuantityShort() { return quantityShort; }
    public void setQuantityShort(int quantityShort) { this.quantityShort = quantityShort; }
    public String getBinLocation() { return binLocation; }
    public void setBinLocation(String binLocation) { this.binLocation = binLocation; }
    public String getItemStatus() { return itemStatus; }
    public void setItemStatus(String itemStatus) { this.itemStatus = itemStatus; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public int getWeightGrams() { return weightGrams; }
    public void setWeightGrams(int weightGrams) { this.weightGrams = weightGrams; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
}
