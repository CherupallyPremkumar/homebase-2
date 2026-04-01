package com.homebase.ecom.promo.model;

import com.homebase.ecom.shared.Money;

import java.util.List;

public class CartSnapshot {
    private String cartId;
    private String userId;
    private int totalItems;
    private Money totalAmount;
    private String region;
    private String customerSegment;
    private List<CartSnapshotItem> items;

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public Money getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Money totalAmount) { this.totalAmount = totalAmount; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getCustomerSegment() { return customerSegment; }
    public void setCustomerSegment(String customerSegment) { this.customerSegment = customerSegment; }
    public List<CartSnapshotItem> getItems() { return items; }
    public void setItems(List<CartSnapshotItem> items) { this.items = items; }

    public int getQuantityByCategory(String category) {
        if (items == null) return 0;
        return items.stream()
                .filter(item -> category != null && category.equals(item.getCategory()))
                .mapToInt(CartSnapshotItem::getQuantity)
                .sum();
    }

    public Money getShippingCost() {
        // Default shipping cost; override as needed
        String currency = (totalAmount != null) ? totalAmount.getCurrency() : "USD";
        return Money.zero(currency);
    }

    public static class CartSnapshotItem {
        private String productId;
        private String category;
        private int quantity;
        private Money price;

        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public Money getPrice() { return price; }
        public void setPrice(Money price) { this.price = price; }
    }
}
