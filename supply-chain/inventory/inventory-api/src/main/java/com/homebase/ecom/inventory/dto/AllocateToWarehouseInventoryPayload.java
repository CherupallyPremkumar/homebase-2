package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Customized Payload for the allocateToWarehouse event.
 * Contains the quantity of stock being allocated to the hub warehouse.
 */
public class AllocateToWarehouseInventoryPayload extends MinimalPayload {

    /** Number of units being allocated to the warehouse for this product. */
    private Integer quantity;
    /** Warehouse or fulfillment center identifier. */
    private String warehouseId;
    /** Storage zone within the warehouse. */
    private String zone;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }
    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }
}
