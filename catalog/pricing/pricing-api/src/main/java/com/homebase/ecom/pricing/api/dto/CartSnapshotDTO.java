package com.homebase.ecom.pricing.api.dto;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Snapshot of a Cart for Pricing Service to consume.
 */
public class CartSnapshotDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String cartId;
    private String userId;
    private String currency;
    private List<CartItemSnapshotDTO> items;
    private String appliedPromoCode;

    public CartSnapshotDTO() {}

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public List<CartItemSnapshotDTO> getItems() { return items; }
    public void setItems(List<CartItemSnapshotDTO> items) { this.items = items; }
    public String getAppliedPromoCode() { return appliedPromoCode; }
    public void setAppliedPromoCode(String appliedPromoCode) { this.appliedPromoCode = appliedPromoCode; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartSnapshotDTO that = (CartSnapshotDTO) o;
        return Objects.equals(cartId, that.cartId) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(currency, that.currency) &&
                Objects.equals(items, that.items) &&
                Objects.equals(appliedPromoCode, that.appliedPromoCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, userId, currency, items, appliedPromoCode);
    }

    @Override
    public String toString() {
        return "CartSnapshotDTO{" +
                "cartId='" + cartId + '\'' +
                ", userId='" + userId + '\'' +
                ", currency='" + currency + '\'' +
                ", items=" + items +
                ", appliedPromoCode='" + appliedPromoCode + '\'' +
                '}';
    }
}
