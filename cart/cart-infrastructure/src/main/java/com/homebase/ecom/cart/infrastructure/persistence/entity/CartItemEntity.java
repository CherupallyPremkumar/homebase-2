package com.homebase.ecom.cart.infrastructure.persistence.entity;

import com.homebase.ecom.cart.model.CartItemStatus;
import com.homebase.ecom.shared.Money;
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

    @Column(name = "quantity")
    private Integer quantity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Column(name = "seller_id")
    private String sellerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CartItemStatus status = CartItemStatus.AVAILABLE;

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public CartItemStatus getStatus() {
        return status;
    }

    public void setStatus(CartItemStatus status) {
        this.status = status;
    }
}
