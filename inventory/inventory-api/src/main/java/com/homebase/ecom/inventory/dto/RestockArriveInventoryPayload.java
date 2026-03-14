package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the restockArrive event.
*/
public class RestockArriveInventoryPayload extends MinimalPayload{

    /** Number of units arriving in the new shipment. */
    private Integer quantity;
    /** Supplier reference for the restock shipment. */
    private String supplierId;

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }
}
