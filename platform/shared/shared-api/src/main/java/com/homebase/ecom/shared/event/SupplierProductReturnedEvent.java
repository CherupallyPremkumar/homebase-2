package com.homebase.ecom.shared.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a SupplierProduct is returned to the seller.
 * This should trigger de-listing or disabling of the corresponding Master
 * Product.
 */
public class SupplierProductReturnedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String EVENT_TYPE = "SUPPLIER_PRODUCT_RETURNED";

    private String eventType = EVENT_TYPE;
    private String supplierProductId;
    private String productId; // The Master Product ID
    private String supplierId;
    private String reason;
    private LocalDateTime returnedAt;

    public SupplierProductReturnedEvent() {
    }

    public SupplierProductReturnedEvent(String supplierProductId, String productId, String supplierId, String reason,
            LocalDateTime returnedAt) {
        this.supplierProductId = supplierProductId;
        this.productId = productId;
        this.supplierId = supplierId;
        this.reason = reason;
        this.returnedAt = returnedAt;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getSupplierProductId() {
        return supplierProductId;
    }

    public void setSupplierProductId(String supplierProductId) {
        this.supplierProductId = supplierProductId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}
