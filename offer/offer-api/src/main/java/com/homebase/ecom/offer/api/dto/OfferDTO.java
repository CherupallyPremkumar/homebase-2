package com.homebase.ecom.offer.api.dto;

import com.homebase.ecom.shared.Money;
import java.io.Serializable;

public class OfferDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String variantId;
    private String supplierId;
    private Money price;
    private Money msrp;
    private String status;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public Money getPrice() { return price; }
    public void setPrice(Money price) { this.price = price; }

    public Money getMsrp() { return msrp; }
    public void setMsrp(Money msrp) { this.msrp = msrp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
