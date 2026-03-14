package com.homebase.ecom.product.event;

/**
 * Event published when a product is approved and ready for public catalog.
 */
public class ProductApprovedEvent {
    private String productId;

    public ProductApprovedEvent() {
    }

    public ProductApprovedEvent(String productId) {
        this.productId = productId;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
}
