package com.homebase.ecom.checkout.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CartSnapshot {
    private final UUID cartId;
    private final List<CartItemVO> items;
    private final BigDecimal subtotal;
    private final int totalQuantity;
    private final LocalDateTime lockedAt;

    public CartSnapshot(UUID cartId, List<CartItemVO> items, BigDecimal subtotal, int totalQuantity, LocalDateTime lockedAt) {
        this.cartId = cartId;
        this.items = items;
        this.subtotal = subtotal;
        this.totalQuantity = totalQuantity;
        this.lockedAt = lockedAt;
    }

    public UUID getCartId() { return cartId; }
    public List<CartItemVO> getItems() { return items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public int getTotalQuantity() { return totalQuantity; }
    public LocalDateTime getLockedAt() { return lockedAt; }

    public static class CartItemVO {
        private final UUID productId;
        private final String productName;
        private final int quantity;
        private final BigDecimal unitPrice;

        public CartItemVO(UUID productId, String productName, int quantity, BigDecimal unitPrice) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public UUID getProductId() { return productId; }
        public String getProductName() { return productName; }
        public int getQuantity() { return quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
    }
}
