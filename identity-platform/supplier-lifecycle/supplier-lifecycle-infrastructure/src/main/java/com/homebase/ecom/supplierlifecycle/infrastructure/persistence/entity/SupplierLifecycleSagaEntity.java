package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity;

import java.util.ArrayList;
import java.util.List;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "supplier_lifecycle_saga")
public class SupplierLifecycleSagaEntity extends AbstractJpaStateEntity {

    @Column(name = "supplier_id", nullable = false)
    private String supplierId;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "reason")
    private String reason;

    @Column(name = "products_affected")
    private int productsAffected;

    @Column(name = "catalog_entries_removed")
    private int catalogEntriesRemoved;

    @Column(name = "inventory_frozen")
    private int inventoryFrozen;

    @Column(name = "orders_cancelled")
    private int ordersCancelled;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "saga_id")
    private List<SupplierLifecycleSagaActivityLogEntity> activities = new ArrayList<>();

    // Getters and Setters
    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public int getProductsAffected() { return productsAffected; }
    public void setProductsAffected(int productsAffected) { this.productsAffected = productsAffected; }

    public int getCatalogEntriesRemoved() { return catalogEntriesRemoved; }
    public void setCatalogEntriesRemoved(int catalogEntriesRemoved) { this.catalogEntriesRemoved = catalogEntriesRemoved; }

    public int getInventoryFrozen() { return inventoryFrozen; }
    public void setInventoryFrozen(int inventoryFrozen) { this.inventoryFrozen = inventoryFrozen; }

    public int getOrdersCancelled() { return ordersCancelled; }
    public void setOrdersCancelled(int ordersCancelled) { this.ordersCancelled = ordersCancelled; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }

    public List<SupplierLifecycleSagaActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<SupplierLifecycleSagaActivityLogEntity> activities) { this.activities = activities; }
}
