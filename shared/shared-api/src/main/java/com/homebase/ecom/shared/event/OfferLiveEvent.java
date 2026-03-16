package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Kafka event published when an offer transitions to LIVE state.
 * Consumed by Pricing BC to update catalog pricing.
 */
public class OfferLiveEvent implements Serializable {

    public static final String EVENT_TYPE = "OFFER_LIVE";

    private String offerId;
    private String productId;
    private String supplierId;
    private String offerType;
    private BigDecimal originalPrice;
    private BigDecimal offerPrice;
    private BigDecimal discountPercent;
    private String eventType = EVENT_TYPE;
    private LocalDateTime timestamp = LocalDateTime.now();

    public OfferLiveEvent() {
    }

    public OfferLiveEvent(String offerId, String productId, String supplierId,
                          String offerType, BigDecimal originalPrice, BigDecimal offerPrice,
                          BigDecimal discountPercent) {
        this.offerId = offerId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.offerType = offerType;
        this.originalPrice = originalPrice;
        this.offerPrice = offerPrice;
        this.discountPercent = discountPercent;
    }

    public String getOfferId() { return offerId; }
    public void setOfferId(String offerId) { this.offerId = offerId; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getOfferType() { return offerType; }
    public void setOfferType(String offerType) { this.offerType = offerType; }

    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }

    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }

    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
