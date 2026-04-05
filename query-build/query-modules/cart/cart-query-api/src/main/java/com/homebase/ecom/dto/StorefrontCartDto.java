package com.homebase.ecom.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Storefront cart view — joins cart + cart_item + product + variant + media + inventory.
 * One row per cart line item with all UI display fields.
 * Used by the storefront UI to render the cart page in a single query.
 */
public class StorefrontCartDto implements Serializable {
    private static final long serialVersionUID = 1L;

    // ── Cart summary ─────────────────────────────────────────────────
    private String cartId;
    private String userId;
    private long subtotal;
    private String currency;
    private String couponCodes;
    private long discountAmount;
    private long total;
    private String stateId;

    // ── Line item ────────────────────────────────────────────────────
    private String itemId;
    private String variantId;
    private String productId;
    private String sku;
    private int quantity;
    private long unitPrice;
    private long lineTotal;

    // ── Product display (from products table) ────────────────────────
    private String productName;
    private String brand;
    private String productSlug;
    private String categoryName;

    // ── Variant display (from variant_attributes) ────────────────────
    private String variantColor;
    private String variantSize;

    // ── Image (from media_assets via variant_media or product_media) ─
    private String imageUrl;

    // ── Stock (from inventory_item) ──────────────────────────────────
    private Integer availableQuantity;
    private String stockStatus;

    // ── Getters & Setters ────────────────────────────────────────────

    public String getCartId() { return cartId; }
    public void setCartId(String cartId) { this.cartId = cartId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getSubtotal() { return subtotal; }
    public void setSubtotal(long subtotal) { this.subtotal = subtotal; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCouponCodes() { return couponCodes; }
    public void setCouponCodes(String couponCodes) { this.couponCodes = couponCodes; }

    public long getDiscountAmount() { return discountAmount; }
    public void setDiscountAmount(long discountAmount) { this.discountAmount = discountAmount; }

    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }

    public String getStateId() { return stateId; }
    public void setStateId(String stateId) { this.stateId = stateId; }

    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(long unitPrice) { this.unitPrice = unitPrice; }

    public long getLineTotal() { return lineTotal; }
    public void setLineTotal(long lineTotal) { this.lineTotal = lineTotal; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getProductSlug() { return productSlug; }
    public void setProductSlug(String productSlug) { this.productSlug = productSlug; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getVariantColor() { return variantColor; }
    public void setVariantColor(String variantColor) { this.variantColor = variantColor; }

    public String getVariantSize() { return variantSize; }
    public void setVariantSize(String variantSize) { this.variantSize = variantSize; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public String getStockStatus() { return stockStatus; }
    public void setStockStatus(String stockStatus) { this.stockStatus = stockStatus; }
}
