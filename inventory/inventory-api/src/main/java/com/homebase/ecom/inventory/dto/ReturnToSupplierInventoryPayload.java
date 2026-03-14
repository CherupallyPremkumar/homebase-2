package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the returnToSupplier event.
*/
public class ReturnToSupplierInventoryPayload extends MinimalPayload{

    /** Number of units being returned to supplier. */
    private Integer returnQuantity;
    /** Supplier identifier. */
    private String supplierId;
    /** Reason for return. */
    private String returnReason;

    public Integer getReturnQuantity() { return returnQuantity; }
    public void setReturnQuantity(Integer returnQuantity) { this.returnQuantity = returnQuantity; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
    public String getReturnReason() { return returnReason; }
    public void setReturnReason(String returnReason) { this.returnReason = returnReason; }
}
