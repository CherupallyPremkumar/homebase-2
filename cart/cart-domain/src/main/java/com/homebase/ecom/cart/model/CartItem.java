package com.homebase.ecom.cart.model;

import com.homebase.ecom.shared.Money;
import org.chenile.utils.entity.model.BaseEntity;

/**
 * Cart item domain model.
 * unitPrice is a Money value object (amount in smallest currency unit).
 */
public class CartItem extends BaseEntity {

    private String cartId;
    private String productId;
    private String variantId;
    private String sku;
    private String productName;
    private String supplierId;
    private int quantity;
    private Money unitPrice = Money.ZERO_INR;
    private Money lineTotal = Money.ZERO_INR;
    private boolean available = true;
    private String tenant;

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(String variantId) {
        this.variantId = variantId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Money getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(Money lineTotal) {
        this.lineTotal = lineTotal;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void recalculateLineTotal() {
        this.lineTotal = unitPrice.multiply(quantity);
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
