package com.homebase.ecom.cart.model;

import com.homebase.ecom.shared.Money;
import org.chenile.utils.entity.model.BaseEntity;
import java.io.Serializable;
import java.util.Date;

public class CartItem extends BaseEntity {

    private String cartId;
    private String productId;
    private Integer quantity;
    private Money price;
    private String sellerId;
    private CartItemStatus status = CartItemStatus.AVAILABLE;

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
