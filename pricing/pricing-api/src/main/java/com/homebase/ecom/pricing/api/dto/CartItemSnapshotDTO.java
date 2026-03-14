package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.util.Objects;

/**
 * Snapshot of a Cart Item for Pricing Service to consume.
 */
public class CartItemSnapshotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String productId;
    private int quantity;
    private Money basePrice;
    private String sellerId;

    public CartItemSnapshotDTO() {}

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Money getBasePrice() { return basePrice; }
    public void setBasePrice(Money basePrice) { this.basePrice = basePrice; }
    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItemSnapshotDTO that = (CartItemSnapshotDTO) o;
        return quantity == that.quantity &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(basePrice, that.basePrice) &&
                Objects.equals(sellerId, that.sellerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity, basePrice, sellerId);
    }

    @Override
    public String toString() {
        return "CartItemSnapshotDTO{" +
                "productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", basePrice=" + basePrice +
                ", sellerId='" + sellerId + '\'' +
                '}';
    }
}
