package com.homebase.ecom.payment.dto;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutSessionRequest {

    private String orderId;
    private BigDecimal amount;
    private String currency;
    private String successUrl;
    private String cancelUrl;
    private List<LineItem> items;

    public CheckoutSessionRequest() {
    }

    public CheckoutSessionRequest(String orderId, BigDecimal amount, String currency, String successUrl,
            String cancelUrl, List<LineItem> items) {
        this.orderId = orderId;
        this.amount = amount;
        this.currency = currency;
        this.successUrl = successUrl;
        this.cancelUrl = cancelUrl;
        this.items = items;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }

    public List<LineItem> getItems() {
        return items;
    }

    public void setItems(List<LineItem> items) {
        this.items = items;
    }

    public static class Builder {
        private String orderId;
        private BigDecimal amount;
        private String currency;
        private String successUrl;
        private String cancelUrl;
        private List<LineItem> items;

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder successUrl(String successUrl) {
            this.successUrl = successUrl;
            return this;
        }

        public Builder cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder items(List<LineItem> items) {
            this.items = items;
            return this;
        }

        public CheckoutSessionRequest build() {
            return new CheckoutSessionRequest(orderId, amount, currency, successUrl, cancelUrl, items);
        }
    }

    public static class LineItem {
        private String productId;
        private String productName;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal totalPrice;

        public LineItem() {
        }

        public LineItem(String productId, String productName, BigDecimal unitPrice, int quantity,
                BigDecimal totalPrice) {
            this.productId = productId;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.totalPrice = totalPrice;
        }

        public static Builder builder() {
            return new Builder();
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public BigDecimal getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }

        public static class Builder {
            private String productId;
            private String productName;
            private BigDecimal unitPrice;
            private int quantity;
            private BigDecimal totalPrice;

            public Builder productId(String productId) {
                this.productId = productId;
                return this;
            }

            public Builder productName(String productName) {
                this.productName = productName;
                return this;
            }

            public Builder unitPrice(BigDecimal unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            public Builder quantity(int quantity) {
                this.quantity = quantity;
                return this;
            }

            public Builder totalPrice(BigDecimal totalPrice) {
                this.totalPrice = totalPrice;
                return this;
            }

            public LineItem build() {
                return new LineItem(productId, productName, unitPrice, quantity, totalPrice);
            }
        }
    }
}
