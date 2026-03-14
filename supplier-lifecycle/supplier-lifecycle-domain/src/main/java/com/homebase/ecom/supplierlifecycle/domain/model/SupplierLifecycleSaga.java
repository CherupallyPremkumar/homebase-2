package com.homebase.ecom.supplierlifecycle.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

/**
 * Aggregate root for the supplier lifecycle saga.
 * Tracks the progress of cascading effects when a supplier is
 * suspended, blacklisted, or reactivated.
 */
public class SupplierLifecycleSaga extends AbstractExtendedStateEntity
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private String id;
    private String supplierId;
    private String action; // SUSPEND, BLACKLIST, REACTIVATE
    private String reason;
    private String flowId;

    // Progress counters
    private int productsAffected;
    private int catalogEntriesRemoved;
    private int inventoryFrozen;
    private int ordersCancelled;

    // Error handling
    private String errorMessage;
    private int retryCount;

    // Workflow related
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getFlowId() { return flowId; }
    public void setFlowId(String flowId) { this.flowId = flowId; }

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

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        SupplierLifecycleSagaActivityLog activityLog = new SupplierLifecycleSagaActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }
}
