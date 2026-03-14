package com.homebase.ecom.shared.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class CartSnapshotDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String cartId;
    private String userId;
    private List<CartItemSnapshot> items;
    private BigDecimal subtotal;
    private String currency;

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<CartItemSnapshot> getItems() { return items; }
    public void setItems(List<CartItemSnapshot> items) { this.items = items; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public static class CartItemSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;
        private String productId;
        private int quantity;
        private BigDecimal unitPrice;

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
