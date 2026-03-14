package com.homebase.ecom.promo.model;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class CartSnapshot implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<CartItem> items;
    private final Money shippingCost;
    private final String region;
    private final String customerId;
    private final String customerSegment;
    private final Map<String, Object> metadata;

    private CartSnapshot(Builder builder) {
        this.items = builder.items;
        this.shippingCost = builder.shippingCost;
        this.region = builder.region;
        this.customerId = builder.customerId;
        this.customerSegment = builder.customerSegment;
        this.metadata = builder.metadata != null ? builder.metadata : Collections.emptyMap();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public Money getShippingCost() {
        return shippingCost;
    }

    public String getRegion() {
        return region;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerSegment() {
        return customerSegment;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<CartItem> items;
        private Money shippingCost;
        private String region;
        private String customerId;
        private String customerSegment;
        private Map<String, Object> metadata = Collections.emptyMap();

        public Builder items(List<CartItem> items) {
            this.items = items;
            return this;
        }

        public Builder shippingCost(Money shippingCost) {
            this.shippingCost = shippingCost;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder customerSegment(String customerSegment) {
            this.customerSegment = customerSegment;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public CartSnapshot build() {
            return new CartSnapshot(this);
        }
    }

    public Money getTotalAmount() {
        if (items == null || items.isEmpty()) {
            return new Money(BigDecimal.ZERO, shippingCost != null ? shippingCost.getCurrency() : "USD");
        }
        String currency = items.get(0).getBasePrice().getCurrency();
        Money total = new Money(BigDecimal.ZERO, currency);
        for (CartItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public int getTotalItems() {
        if (items == null)
            return 0;
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public int getQuantityByCategory(String category) {
        if (items == null)
            return 0;
        return items.stream()
                .filter(item -> category.equals(item.getCategory()))
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public Money getAveragePriceForCategory(String category) {
        if (items == null)
            return null;
        String currency = null;
        BigDecimal totalAmount = BigDecimal.ZERO;
        int totalQuantity = 0;

        for (CartItem item : items) {
            if (category.equals(item.getCategory())) {
                if (currency == null)
                    currency = item.getBasePrice().getCurrency();
                totalAmount = totalAmount.add(item.getSubtotal().getAmount());
                totalQuantity += item.getQuantity();
            }
        }

        if (totalQuantity == 0)
            return null;
        return new Money(totalAmount.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP), currency);
    }
}
