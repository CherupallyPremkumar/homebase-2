package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the approveStock event.
 * Contains the approved quantity of stock units.
 */
public class ApproveStockInventoryPayload extends MinimalPayload {

    /** Number of units being approved into the system. */
    private Integer quantity;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
