package com.homebase.ecom.order.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import com.homebase.ecom.shared.model.Money;

@Entity
@Table(name = "order_items", indexes = {
        @Index(name = "idx_order_items_order", columnList = "order_id")
})
public class OrderItem extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Column(name = "product_name")
    private String productName;

    @Column(nullable = false)
    private Integer quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "unit_price", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money unitPrice;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_price", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3, insertable = false, updatable = false))
    })
    private Money totalPrice;

    // --- Getters & Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Money unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Money totalPrice) {
        this.totalPrice = totalPrice;
    }
}
