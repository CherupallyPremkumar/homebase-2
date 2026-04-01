package com.homebase.ecom.pricing.domain.model;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class LineItemDiscount implements Serializable {
    private static final long serialVersionUID = 1L;
    private UUID cartItemId;
    private UUID productId;
    private String productName;
    private Money discountAmount;
    private String discountReason;

    public LineItemDiscount() {}

    public UUID getCartItemId() { return cartItemId; }
    public void setCartItemId(UUID cartItemId) { this.cartItemId = cartItemId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Money getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(Money discountAmount) { this.discountAmount = discountAmount; }
    public String getDiscountReason() { return discountReason; }
    public void setDiscountReason(String discountReason) { this.discountReason = discountReason; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineItemDiscount that = (LineItemDiscount) o;
        return Objects.equals(cartItemId, that.cartItemId) &&
                Objects.equals(productId, that.productId) &&
                Objects.equals(productName, that.productName) &&
                Objects.equals(discountAmount, that.discountAmount) &&
                Objects.equals(discountReason, that.discountReason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemId, productId, productName, discountAmount, discountReason);
    }

    @Override
    public String toString() {
        return "LineItemDiscount{" +
                "cartItemId=" + cartItemId +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", discountAmount=" + discountAmount +
                ", discountReason='" + discountReason + '\'' +
                '}';
    }
}
