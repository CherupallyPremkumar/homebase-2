package com.homebase.ecom.product.model;

import jakarta.persistence.*;
import org.chenile.jpautils.entity.BaseJpaEntity;
import com.homebase.ecom.shared.model.Money;

@Entity
@Table(name = "offers", indexes = {
        @Index(name = "idx_offers_product_id", columnList = "product_id"),
        @Index(name = "idx_offers_seller_id", columnList = "seller_id")
})
public class Offer extends BaseJpaEntity {

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "seller_id", nullable = false)
    private String sellerId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", length = 3))
    })
    private Money price;

    @Column(nullable = false)
    private Boolean active = true;

    // --- Getters & Setters ---

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
