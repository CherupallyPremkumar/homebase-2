package com.homebase.ecom.returnrequest.dto;

import org.chenile.workflow.param.MinimalPayload;

/**
 * Payload for the receiveItem event (APPROVED/PARTIALLY_APPROVED -> ITEM_RECEIVED).
 * Warehouse confirms receipt of the returned item.
 */
public class ReceiveItemReturnrequestPayload extends MinimalPayload {

    private String warehouseId;
    private String conditionOnReceipt;

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getConditionOnReceipt() { return conditionOnReceipt; }
    public void setConditionOnReceipt(String conditionOnReceipt) { this.conditionOnReceipt = conditionOnReceipt; }
}
