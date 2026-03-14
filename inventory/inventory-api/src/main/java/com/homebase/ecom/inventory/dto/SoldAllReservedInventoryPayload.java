package com.homebase.ecom.inventory.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
    Customized Payload for the soldAllReserved event.
*/
public class SoldAllReservedInventoryPayload extends MinimalPayload{

    private int quantity;
    private String orderId;

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
}
