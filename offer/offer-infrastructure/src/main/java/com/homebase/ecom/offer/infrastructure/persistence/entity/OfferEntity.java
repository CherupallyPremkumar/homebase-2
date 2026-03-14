package com.homebase.ecom.offer.infrastructure.persistence.entity;

import com.homebase.ecom.shared.Money;
import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "offer")
public class OfferEntity extends AbstractJpaStateEntity {

    @Column(name = "variant_id")
    private String variantId;

    @Column(name = "supplier_id")
    private String supplierId;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "amount", column = @Column(name = "msrp_amount")),
        @AttributeOverride(name = "currency", column = @Column(name = "msrp_currency"))
    })
    private Money msrp;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "offer_id")
    private List<OfferActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public Money getPrice() { return price; }
    public void setPrice(Money price) { this.price = price; }

    public Money getMsrp() { return msrp; }
    public void setMsrp(Money msrp) { this.msrp = msrp; }

    public List<OfferActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<OfferActivityLogEntity> activities) { this.activities = activities; }
}
