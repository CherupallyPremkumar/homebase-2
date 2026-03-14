package com.homebase.ecom.checkout.api.dto;

import java.math.BigDecimal;

/**
 * Cart Item DTO for price calculation response
 */
public class CartItemDTO {

    private String productId;
    private String sku;
    private String name;
    private Integer quantity;
    private MoneyDTO unitPrice;
    private MoneyDTO subtotal;
    private MoneyDTO discount;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public MoneyDTO getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(MoneyDTO subtotal) {
        this.subtotal = subtotal;
    }

    public MoneyDTO getDiscount() {
        return discount;
    }

    public void setDiscount(MoneyDTO discount) {
        this.discount = discount;
    }

    public MoneyDTO getTotal() {
        return total;
    }

    public void setTotal(MoneyDTO total) {
        this.total = total;
    }
}
