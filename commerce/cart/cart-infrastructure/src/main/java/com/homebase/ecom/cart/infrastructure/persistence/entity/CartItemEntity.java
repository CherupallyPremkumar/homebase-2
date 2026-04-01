package com.homebase.ecom.cart.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;

@Entity
@Table(name = "cart_item")
public class CartItemEntity extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "variant_id")
    private String variantId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private long unitPrice;

    @Column(name = "line_total")
    private long lineTotal;

    @Column(name = "available")
    private boolean available = true;

    @Column(name = "saved_for_later")
    private boolean savedForLater;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "original_price")
    private Long originalPrice;

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
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

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public long getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(long lineTotal) {
        this.lineTotal = lineTotal;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isSavedForLater() {
        return savedForLater;
    }

    public void setSavedForLater(boolean savedForLater) {
        this.savedForLater = savedForLater;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Long originalPrice) {
        this.originalPrice = originalPrice;
    }
}
