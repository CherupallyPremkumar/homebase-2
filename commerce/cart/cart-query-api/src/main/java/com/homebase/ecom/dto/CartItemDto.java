package com.homebase.ecom.dto;

import java.io.Serializable;
import java.util.Date;

public class CartItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String cartId;
    private String productId;
    private String variantId;
    private String sku;
    private String productName;
    private String supplierId;
    private int quantity;
    private long unitPrice;
    private long lineTotal;
    private Date createdTime;
    private Date lastModifiedTime;
    private String tenant;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(long unitPrice) { this.unitPrice = unitPrice; }
    public long getLineTotal() { return lineTotal; }
    public void setLineTotal(long lineTotal) { this.lineTotal = lineTotal; }
    public Date getCreatedTime() { return createdTime; }
    public void setCreatedTime(Date createdTime) { this.createdTime = createdTime; }
    public Date getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(Date lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
