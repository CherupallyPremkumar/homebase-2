package com.homebase.ecom.cart.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import java.io.Serializable;

@Entity
@Table(name = "cart_item")
public class CartItem extends BaseJpaEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(nullable = false)
    private Integer quantity;

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
