package com.homebase.ecom.promo.model;

import com.homebase.ecom.shared.Money;

import java.io.Serializable;
import java.util.UUID;

public final class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private final UUID cartItemId;
    private final UUID productId;
    private final String productName;
    private final String category;
    private final String sku;
    private final int quantity;
    private final Money basePrice;

    public CartItem(UUID cartItemId, UUID productId, String productName, String category, String sku, int quantity,
            Money basePrice) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.sku = sku;
        this.quantity = quantity;
        this.basePrice = basePrice;
    }

    public UUID getCartItemId() {
        return cartItemId;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getCategory() {
        return category;
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public Money getBasePrice() {
        return basePrice;
    }

    public Money getSubtotal() {
        return basePrice.multiply(java.math.BigDecimal.valueOf(quantity));
    }

    public Money getTotalPrice() {
        return getSubtotal();
    }
}
