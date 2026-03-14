package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the itemReceived event.
 * Carries warehouse receipt details and product condition.
 */
public class ItemReceivedReturnrequestPayload extends MinimalPayload {

    private String warehouseId;
    private String conditionOnReceipt;

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getConditionOnReceipt() { return conditionOnReceipt; }
    public void setConditionOnReceipt(String conditionOnReceipt) { this.conditionOnReceipt = conditionOnReceipt; }
}
