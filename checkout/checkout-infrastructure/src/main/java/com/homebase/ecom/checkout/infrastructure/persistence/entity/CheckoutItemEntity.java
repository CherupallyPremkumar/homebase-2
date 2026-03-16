package com.homebase.ecom.checkout.infrastructure.persistence.entity;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaEntity;

@Entity
@Table(name = "checkout_item")
public class CheckoutItemEntity extends AbstractJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checkout_id")
    private CheckoutEntity checkout;

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

    // ── Getters & Setters ──────────────────────────────────────────────

    public CheckoutEntity getCheckout() { return checkout; }
    public void setCheckout(CheckoutEntity checkout) { this.checkout = checkout; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(long unitPrice) { this.unitPrice = unitPrice; }

    public long getLineTotal() { return lineTotal; }
    public void setLineTotal(long lineTotal) { this.lineTotal = lineTotal; }
}
