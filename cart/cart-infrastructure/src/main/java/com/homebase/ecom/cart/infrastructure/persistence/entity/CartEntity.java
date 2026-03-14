package com.homebase.ecom.cart.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import jakarta.persistence.*;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class CartEntity extends AbstractJpaStateEntity {

    @Column(name = "user_id")
    private String userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "cart")
    private List<CartItemEntity> items = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "total_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "total_currency"))
    })
    private Money totalAmount;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "applied_promo_code")
    private String appliedPromoCode;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "discount_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "discount_currency"))
    })
    private Money discountAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "tax_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "tax_currency"))
    })
    private Money taxAmount;

    @Column(name = "description")
    public String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private List<CartActivityLogEntity> activities = new ArrayList<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CartItemEntity> getItems() {
        return items;
    }

    public void setItems(List<CartItemEntity> items) {
        this.items = items;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getAppliedPromoCode() {
        return appliedPromoCode;
    }

    public void setAppliedPromoCode(String appliedPromoCode) {
        this.appliedPromoCode = appliedPromoCode;
    }

    public Money getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Money discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount = taxAmount;
    }

    public List<CartActivityLogEntity> getActivities() {
        return activities;
    }

    public void setActivities(List<CartActivityLogEntity> activities) {
        this.activities = activities;
    }
}
