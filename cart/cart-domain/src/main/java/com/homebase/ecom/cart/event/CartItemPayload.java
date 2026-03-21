package com.homebase.ecom.cart.event;

import java.io.Serializable;

/**
 * Lightweight item snapshot embedded in cart events.
 * Carries only the fields downstream consumers need.
 */
public class CartItemPayload implements Serializable {

    private final String productId;
    private final String sku;
    private final String productName;
    private final int quantity;
    private final long unitPrice;
    private final String supplierId;

    public CartItemPayload(String productId, String sku, String productName,
            int quantity, long unitPrice, String supplierId) {
        this.productId = productId;
        this.sku = sku;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.supplierId = supplierId;
    }

    public String getProductId() { return productId; }
    public String getSku() { return sku; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public long getUnitPrice() { return unitPrice; }
    public String getSupplierId() { return supplierId; }
}
