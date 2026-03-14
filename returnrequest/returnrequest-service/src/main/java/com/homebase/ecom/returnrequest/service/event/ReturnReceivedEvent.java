package com.homebase.ecom.returnrequest.service.event;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Event published when a returned item is received at the warehouse.
 */
public class ReturnReceivedEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private String returnRequestId;
    private String orderId;
    private String warehouseId;
    private String conditionOnReceipt;
    private LocalDateTime receivedAt;

    public ReturnReceivedEvent() {}

    public ReturnReceivedEvent(String returnRequestId, String orderId, String warehouseId,
                               String conditionOnReceipt) {
        this.returnRequestId = returnRequestId;
        this.orderId = orderId;
        this.warehouseId = warehouseId;
        this.conditionOnReceipt = conditionOnReceipt;
        this.receivedAt = LocalDateTime.now();
    }

    public String getReturnRequestId() { return returnRequestId; }
    public void setReturnRequestId(String returnRequestId) { this.returnRequestId = returnRequestId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getWarehouseId() { return warehouseId; }
    public void setWarehouseId(String warehouseId) { this.warehouseId = warehouseId; }

    public String getConditionOnReceipt() { return conditionOnReceipt; }
    public void setConditionOnReceipt(String conditionOnReceipt) { this.conditionOnReceipt = conditionOnReceipt; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }
}
