package com.homebase.ecom.checkout.api.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Order Response DTO
 */
public class OrderResponseDTO {

    private String orderId;
    private String orderNumber;
    private String status;
    private MoneyDTO total;
    private List<OrderItemDTO> items;

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MoneyDTO getTotal() {
        return total;
    }

    public void setTotal(MoneyDTO total) {
        this.total = total;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    public static class OrderItemDTO {
        private String productId;
        private String sku;
        private Integer quantity;
        private MoneyDTO unitPrice;
        private MoneyDTO total;

        // Getters and Setters
        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public MoneyDTO getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(MoneyDTO unitPrice) {
            this.unitPrice = unitPrice;
        }

        public MoneyDTO getTotal() {
            return total;
        }

        public void setTotal(MoneyDTO total) {
            this.total = total;
        }
    }
}
