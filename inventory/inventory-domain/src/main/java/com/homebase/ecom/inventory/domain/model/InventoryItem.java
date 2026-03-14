package com.homebase.ecom.inventory.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.chenile.workflow.activities.model.ActivityEnabledStateEntity;
import org.chenile.workflow.activities.model.ActivityLog;
import org.chenile.utils.entity.model.AbstractExtendedStateEntity;
import org.chenile.workflow.model.ContainsTransientMap;
import org.chenile.workflow.model.TransientMap;

public class InventoryItem extends AbstractExtendedStateEntity 
        implements ActivityEnabledStateEntity, ContainsTransientMap {

    private static final Logger log = LoggerFactory.getLogger(InventoryItem.class);

    // Core Identity
    private String id;
    private String sku;
    private String asin;
    private String productId;
    
    // Stock Information
    private Integer quantity = 0;
    private Integer availableQuantity = 0;
    private Integer reservedQuantity = 0;
    private Integer inboundQuantity = 0;
    private Integer damagedQuantity = 0;
    
    // Fulfillment Configuration
    private String primaryFulfillmentCenter;
    private Boolean isFbaEnabled = false;
    private Boolean isMerchantFulfilled = true;
    
    // Thresholds & Status
    private Integer lowStockThreshold = 10;
    private Integer outOfStockThreshold = 0;
    private InventoryStatus status = InventoryStatus.AVAILABLE;
    
    // Reservations
    private List<Reservation> activeReservations = new ArrayList<>();
    
    // Stock History
    private List<StockMovement> movementHistory = new ArrayList<>();
    
    // Workflow related
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();

    // Timestamps
    private Instant createdAt;
    private Instant updatedAt;
    private Instant lastSaleAt;
    private Instant lastRestockAt;

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(Integer availableQuantity) { this.availableQuantity = availableQuantity; }

    public Integer getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(Integer reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public Integer getInboundQuantity() { return inboundQuantity; }
    public void setInboundQuantity(Integer inboundQuantity) { this.inboundQuantity = inboundQuantity; }

    public Integer getDamagedQuantity() { return damagedQuantity; }
    public void setDamagedQuantity(Integer damagedQuantity) { this.damagedQuantity = damagedQuantity; }

    public String getPrimaryFulfillmentCenter() { return primaryFulfillmentCenter; }
    public void setPrimaryFulfillmentCenter(String primaryFulfillmentCenter) { this.primaryFulfillmentCenter = primaryFulfillmentCenter; }

    public Boolean getIsFbaEnabled() { return isFbaEnabled; }
    public void setIsFbaEnabled(Boolean isFbaEnabled) { this.isFbaEnabled = isFbaEnabled; }

    public Boolean getIsMerchantFulfilled() { return isMerchantFulfilled; }
    public void setIsMerchantFulfilled(Boolean isMerchantFulfilled) { this.isMerchantFulfilled = isMerchantFulfilled; }

    public Integer getLowStockThreshold() { return lowStockThreshold; }
    public void setLowStockThreshold(Integer lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }

    public Integer getOutOfStockThreshold() { return outOfStockThreshold; }
    public void setOutOfStockThreshold(Integer outOfStockThreshold) { this.outOfStockThreshold = outOfStockThreshold; }

    public InventoryStatus getStatus() { return status; }
    public void setStatus(InventoryStatus status) { this.status = status; }

    public List<Reservation> getActiveReservations() { return activeReservations; }
    public void setActiveReservations(List<Reservation> activeReservations) { this.activeReservations = activeReservations; }

    public List<StockMovement> getMovementHistory() { return movementHistory; }
    public void setMovementHistory(List<StockMovement> movementHistory) { this.movementHistory = movementHistory; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getLastSaleAt() { return lastSaleAt; }
    public void setLastSaleAt(Instant lastSaleAt) { this.lastSaleAt = lastSaleAt; }

    public Instant getLastRestockAt() { return lastRestockAt; }
    public void setLastRestockAt(Instant lastRestockAt) { this.lastRestockAt = lastRestockAt; }

    // Business Logic
    public void reserveStock(int qty, String orderId, String sessionId, int ttlMinutes) {
        Reservation reservation = new Reservation(orderId, sessionId, qty, 
            Instant.now(), Instant.now().plusSeconds(ttlMinutes * 60L), ReservationStatus.RESERVED);
        this.activeReservations.add(reservation);
        this.reservedQuantity += qty;
        this.availableQuantity -= qty;
        this.updatedAt = Instant.now();
    }

    public void releaseReservation(String orderId) {
        this.activeReservations.stream()
            .filter(r -> r.orderId().equals(orderId) && r.status() == ReservationStatus.RESERVED)
            .findFirst()
            .ifPresent(r -> {
                // In a real record-based implementation, we might need to replace the reservation in the list
                // OR use a mutable entity if needed. But let's assume we replace it or just mark for simplicity
                // since we are using Records for value objects.
                this.activeReservations.remove(r);
                this.activeReservations.add(new Reservation(r.orderId(), r.sessionId(), r.quantity(), 
                    r.reservedAt(), r.expiresAt(), ReservationStatus.CANCELLED));
                this.reservedQuantity -= r.quantity();
                this.availableQuantity += r.quantity();
            });
        this.updatedAt = Instant.now();
    }

    public void confirmReservation(String orderId) {
        this.activeReservations.stream()
            .filter(r -> r.orderId().equals(orderId) && r.status() == ReservationStatus.RESERVED)
            .findFirst()
            .ifPresent(r -> {
                this.activeReservations.remove(r);
                this.activeReservations.add(new Reservation(r.orderId(), r.sessionId(), r.quantity(), 
                    r.reservedAt(), r.expiresAt(), ReservationStatus.FULFILLED));
                this.reservedQuantity -= r.quantity();
                this.quantity -= r.quantity();
                
                addMovement(MovementType.PICK, r.quantity(), orderId, primaryFulfillmentCenter, "Order fulfilled");
            });
        this.updatedAt = Instant.now();
    }

    public void releaseExpiredReservations(Instant cutoff) {
        List<Reservation> expired = this.activeReservations.stream()
            .filter(r -> r.status() == ReservationStatus.RESERVED && r.reservedAt().isBefore(cutoff))
            .toList();
        
        for (Reservation r : expired) {
            this.activeReservations.remove(r);
            this.activeReservations.add(new Reservation(r.orderId(), r.sessionId(), r.quantity(), 
                r.reservedAt(), r.expiresAt(), ReservationStatus.EXPIRED));
            this.reservedQuantity -= r.quantity();
            this.availableQuantity += r.quantity();
            log.info("Expired reservation released for orderId={}, qty={}, productId={}", 
                r.orderId(), r.quantity(), this.productId);
        }
        if (!expired.isEmpty()) {
            this.updatedAt = Instant.now();
        }
    }

    public void addStock(int qty, String referenceId, String reason) {
        this.quantity += qty;
        this.availableQuantity += qty;
        addMovement(MovementType.RECEIVE, qty, referenceId, primaryFulfillmentCenter, reason);
        this.lastRestockAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    /**
     * Records damage found during inspection. Adjusts damaged quantity and total quantity.
     */
    public void recordDamage(int damagedQty) {
        this.damagedQuantity += damagedQty;
        addMovement(MovementType.DAMAGED, damagedQty, null, primaryFulfillmentCenter, "Damage found during inspection");
        this.updatedAt = Instant.now();
    }

    /**
     * Records damage discovered while stock is in the warehouse.
     */
    public void recordWarehouseDamage(int damagedQty) {
        this.damagedQuantity += damagedQty;
        this.availableQuantity -= damagedQty;
        addMovement(MovementType.DAMAGED, damagedQty, null, primaryFulfillmentCenter, "Damage found in warehouse");
        this.updatedAt = Instant.now();
    }

    /**
     * Repairs damaged units, moving them back to available inventory.
     */
    public void repairDamaged(int repairedQty) {
        int toRepair = repairedQty > 0 ? Math.min(repairedQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toRepair;
        this.availableQuantity += toRepair;
        addMovement(MovementType.ADJUSTMENT, toRepair, null, primaryFulfillmentCenter, "Repaired damaged units");
        this.updatedAt = Instant.now();
    }

    /**
     * Repairs damaged units at the PARTIAL_DAMAGE stage (pre-warehouse allocation).
     * Damaged units are moved back to the total quantity pool, not to availableQuantity
     * since the stock has not yet been allocated to a warehouse.
     */
    public void repairDamagedPreAllocation(int repairedQty) {
        int toRepair = repairedQty > 0 ? Math.min(repairedQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toRepair;
        addMovement(MovementType.ADJUSTMENT, toRepair, null, primaryFulfillmentCenter, "Repaired damaged units (pre-allocation)");
        this.updatedAt = Instant.now();
    }

    /**
     * Discards damaged units, writing them off from inventory.
     */
    public void discardDamaged(int discardQty) {
        int toDiscard = discardQty > 0 ? Math.min(discardQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toDiscard;
        this.quantity -= toDiscard;
        addMovement(MovementType.ADJUSTMENT, -toDiscard, null, primaryFulfillmentCenter, "Discarded damaged units");
        this.updatedAt = Instant.now();
    }

    /**
     * Marks stock for return to supplier. Adjusts quantities accordingly.
     */
    public void initiateSupplierReturn(int returnQty) {
        int toReturn = returnQty > 0 ? returnQty : this.damagedQuantity;
        this.quantity -= toReturn;
        if (this.damagedQuantity >= toReturn) {
            this.damagedQuantity -= toReturn;
        }
        addMovement(MovementType.RETURNED, toReturn, null, primaryFulfillmentCenter, "Return to supplier initiated");
        this.updatedAt = Instant.now();
    }

    /**
     * Marks all remaining available stock as sold. Sets available to 0.
     */
    public void sellAllStock() {
        int soldQty = this.availableQuantity;
        this.availableQuantity = 0;
        this.quantity -= soldQty;
        this.lastSaleAt = Instant.now();
        addMovement(MovementType.PICK, soldQty, null, primaryFulfillmentCenter, "All stock sold");
        this.updatedAt = Instant.now();
    }

    /**
     * Resets quantities for a new stock cycle (restock after out of stock).
     */
    public void resetForRestock(int newQty) {
        this.inboundQuantity = newQty;
        this.quantity = newQty;
        this.availableQuantity = 0;
        this.reservedQuantity = 0;
        this.damagedQuantity = 0;
        this.activeReservations.clear();
        this.lastRestockAt = Instant.now();
        addMovement(MovementType.RECEIVING, newQty, null, primaryFulfillmentCenter, "New stock cycle started");
        this.updatedAt = Instant.now();
    }

    /**
     * Checks if available quantity is below low stock threshold.
     */
    public boolean isLowStock() {
        return this.availableQuantity <= (this.lowStockThreshold != null ? this.lowStockThreshold : 10);
    }

    /**
     * Checks if inventory is fully depleted.
     */
    public boolean isOutOfStock() {
        return this.availableQuantity <= 0 && this.reservedQuantity <= 0;
    }

    /**
     * Calculates damage percentage relative to total quantity.
     */
    public int getDamagePercentage() {
        if (this.quantity == 0) return 100;
        return (int) (((double) this.damagedQuantity / this.quantity) * 100);
    }

    private void addMovement(MovementType type, int qty, String refId, String fcId, String reason) {
        StockMovement movement = new StockMovement(type, qty, refId, fcId, Instant.now(), reason);
        this.movementHistory.add(movement);
    }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    @Override
    public Collection<ActivityLog> obtainActivities() {
        return new ArrayList<>(activities);
    }

    @Override
    public ActivityLog addActivity(String eventId, String comment) {
        InventoryActivityLog activityLog = new InventoryActivityLog();
        activityLog.activityName = eventId;
        activityLog.activityComment = comment;
        activityLog.activitySuccess = true;
        this.activities.add(activityLog);
        return activityLog;
    }
}
