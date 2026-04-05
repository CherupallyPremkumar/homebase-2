package com.homebase.ecom.pricing.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PriceHistoryDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String variantId;
    private BigDecimal price;
    private String currency;
    private String changedBy;
    private Date createdAt;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
