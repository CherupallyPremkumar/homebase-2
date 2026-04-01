package com.homebase.ecom.cart.dto;

import java.io.Serializable;

public class CartItemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private String variantId;
    private String sku;
    private String productName;
    private String supplierId;
    private int quantity;
    private long unitPrice;
    private long lineTotal;
    private boolean available = true;
    private boolean savedForLater;
    private String imageUrl;
    private Long originalPrice;

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

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public boolean isSavedForLater() { return savedForLater; }
    public void setSavedForLater(boolean savedForLater) { this.savedForLater = savedForLater; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Long getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(Long originalPrice) { this.originalPrice = originalPrice; }
}
