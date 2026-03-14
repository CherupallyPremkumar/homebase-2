package com.homebase.ecom.catalog.domain.port.out;

import java.math.BigDecimal;

public interface OfferClientPort {
    OfferOverview fetchOfferDetails(String offerId);

    class OfferOverview {
        private String id;
        private String variantId; // Maps to variantId internally in Offer
        private BigDecimal price;
        private boolean active;

        // Getters and Setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVariantId() {
            return variantId;
        }

        public void setVariantId(String variantId) {
            this.variantId = variantId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }
    }
}
