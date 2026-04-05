package com.homebase.ecom.supplierlifecycle.dto;

/**
 * Request payload for supplier lifecycle orchestration actions.
 */
public class SupplierLifecycleRequest {

    private String supplierId;
    private SupplierLifecycleAction action;
    private String reason;

    public enum SupplierLifecycleAction {
        SUSPEND,
        BLACKLIST,
        REACTIVATE
    }

    public SupplierLifecycleRequest() {
    }

    public SupplierLifecycleRequest(String supplierId, SupplierLifecycleAction action, String reason) {
        this.supplierId = supplierId;
        this.action = action;
        this.reason = reason;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public SupplierLifecycleAction getAction() {
        return action;
    }

    public void setAction(SupplierLifecycleAction action) {
        this.action = action;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
