package com.homebase.ecom.supplierlifecycle.dto;

/**
 * Response payload for supplier lifecycle orchestration results.
 */
public class SupplierLifecycleResponse {

    private String supplierId;
    private int productsAffected;
    private int catalogEntriesRemoved;
    private int inventoryFrozen;
    private String status;

    public SupplierLifecycleResponse() {
    }

    public SupplierLifecycleResponse(String supplierId, int productsAffected,
                                     int catalogEntriesRemoved, int inventoryFrozen, String status) {
        this.supplierId = supplierId;
        this.productsAffected = productsAffected;
        this.catalogEntriesRemoved = catalogEntriesRemoved;
        this.inventoryFrozen = inventoryFrozen;
        this.status = status;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public int getProductsAffected() {
        return productsAffected;
    }

    public void setProductsAffected(int productsAffected) {
        this.productsAffected = productsAffected;
    }

    public int getCatalogEntriesRemoved() {
        return catalogEntriesRemoved;
    }

    public void setCatalogEntriesRemoved(int catalogEntriesRemoved) {
        this.catalogEntriesRemoved = catalogEntriesRemoved;
    }

    public int getInventoryFrozen() {
        return inventoryFrozen;
    }

    public void setInventoryFrozen(int inventoryFrozen) {
        this.inventoryFrozen = inventoryFrozen;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
