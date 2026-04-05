package com.homebase.ecom.inventory.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
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

    // Core Identity (id inherited from BaseEntity)
    private String description;
    private String sku;
    private String asin;
    private String productId;
    private String variantId;
    
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
    
    // Supply chain fields (from inventory-006 migration)
    private String supplierId;
    private BigDecimal costPrice;
    private String batchNumber;
    private LocalDate expiryDate;
    private String shelfLocation;

    // Domain-specific Timestamps (createdTime/lastModifiedTime inherited from BaseEntity)
    private Instant lastSaleAt;
    private Instant lastRestockAt;

    // Reservations
    private List<Reservation> activeReservations = new ArrayList<>();
    
    // Stock History
    private List<StockMovement> movementHistory = new ArrayList<>();

    // Damage tracking — individual unit-level records
    private List<DamageRecord> damageRecords = new ArrayList<>();
    
    private String tenant;

    // Workflow related
    private transient TransientMap transientMap = new TransientMap();
    private List<ActivityLog> activities = new ArrayList<>();


    // Getters and Setters (id, createdTime, lastModifiedTime inherited from BaseEntity)

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getAsin() { return asin; }
    public void setAsin(String asin) { this.asin = asin; }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getVariantId() { return variantId; }
    public void setVariantId(String variantId) { this.variantId = variantId; }

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

    /**
     * Computed property — derives status from quantities, never set manually.
     * STM state handles workflow routing; this is for API/query consumers.
     */
    public InventoryStatus getStatus() {
        if (this.status == InventoryStatus.DISCONTINUED) return InventoryStatus.DISCONTINUED;
        if (isOutOfStock()) return InventoryStatus.OUT_OF_STOCK;
        if (isLowStock()) return InventoryStatus.LOW_STOCK;
        return InventoryStatus.AVAILABLE;
    }
    public void setStatus(InventoryStatus status) { this.status = status; }

    public List<Reservation> getActiveReservations() { return activeReservations; }
    public void setActiveReservations(List<Reservation> activeReservations) { this.activeReservations = activeReservations; }

    public List<StockMovement> getMovementHistory() { return movementHistory; }
    public void setMovementHistory(List<StockMovement> movementHistory) { this.movementHistory = movementHistory; }

    public List<DamageRecord> getDamageRecords() { return damageRecords; }
    public void setDamageRecords(List<DamageRecord> damageRecords) { this.damageRecords = damageRecords; }

    public String getSupplierId() { return supplierId; }
    public void setSupplierId(String supplierId) { this.supplierId = supplierId; }

    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public String getShelfLocation() { return shelfLocation; }
    public void setShelfLocation(String shelfLocation) { this.shelfLocation = shelfLocation; }

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
        this.setLastModifiedTime(new java.util.Date());
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
        this.setLastModifiedTime(new java.util.Date());
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
        this.setLastModifiedTime(new java.util.Date());
    }

    public List<String> getExpiredOrderIds(Instant cutoff) {
        return this.activeReservations.stream()
            .filter(r -> r.status() == ReservationStatus.RESERVED && r.reservedAt().isBefore(cutoff))
            .map(Reservation::orderId)
            .toList();
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
            this.setLastModifiedTime(new java.util.Date());
        }
    }

    public void addStock(int qty, String referenceId, String reason) {
        this.quantity += qty;
        this.availableQuantity += qty;
        addMovement(MovementType.RECEIVE, qty, referenceId, primaryFulfillmentCenter, reason);
        this.lastRestockAt = Instant.now();
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Records damage found during inspection. Adjusts damaged quantity and total quantity.
     * Creates individual DamageRecord entries for each damaged unit.
     */
    public void recordDamage(int damagedQty, List<DamageRecord> unitDamages) {
        this.damagedQuantity += damagedQty;
        if (unitDamages != null) {
            this.damageRecords.addAll(unitDamages);
        }
        addMovement(MovementType.DAMAGED, damagedQty, null, primaryFulfillmentCenter, "Damage found during inspection");
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Records damage discovered while stock is in the warehouse.
     * Creates individual DamageRecord entries for each damaged unit.
     */
    public void recordWarehouseDamage(int damagedQty, List<DamageRecord> unitDamages) {
        this.damagedQuantity += damagedQty;
        this.availableQuantity -= damagedQty;
        if (unitDamages != null) {
            this.damageRecords.addAll(unitDamages);
        }
        addMovement(MovementType.DAMAGED, damagedQty, null, primaryFulfillmentCenter, "Damage found in warehouse");
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Repairs damaged units, moving them back to available inventory.
     * Updates matching REPORTED damage records to REPAIRED status.
     */
    public void repairDamaged(int repairedQty, List<String> unitIdentifiers) {
        int toRepair = repairedQty > 0 ? Math.min(repairedQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toRepair;
        this.availableQuantity += toRepair;
        updateDamageRecordStatuses(unitIdentifiers, DamageStatus.REPAIRED, toRepair);
        addMovement(MovementType.ADJUSTMENT, toRepair, null, primaryFulfillmentCenter, "Repaired damaged units");
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Repairs damaged units at the PARTIAL_DAMAGE stage (pre-warehouse allocation).
     * Damaged units are moved back to the total quantity pool, not to availableQuantity
     * since the stock has not yet been allocated to a warehouse.
     */
    public void repairDamagedPreAllocation(int repairedQty, List<String> unitIdentifiers) {
        int toRepair = repairedQty > 0 ? Math.min(repairedQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toRepair;
        updateDamageRecordStatuses(unitIdentifiers, DamageStatus.REPAIRED, toRepair);
        addMovement(MovementType.ADJUSTMENT, toRepair, null, primaryFulfillmentCenter, "Repaired damaged units (pre-allocation)");
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Discards damaged units, writing them off from inventory.
     */
    public void discardDamaged(int discardQty, List<String> unitIdentifiers) {
        int toDiscard = discardQty > 0 ? Math.min(discardQty, this.damagedQuantity) : this.damagedQuantity;
        this.damagedQuantity -= toDiscard;
        this.quantity -= toDiscard;
        updateDamageRecordStatuses(unitIdentifiers, DamageStatus.DISCARDED, toDiscard);
        addMovement(MovementType.ADJUSTMENT, -toDiscard, null, primaryFulfillmentCenter, "Discarded damaged units");
        this.setLastModifiedTime(new java.util.Date());
    }

    /**
     * Marks stock for return to supplier. Adjusts quantities accordingly.
     */
    public void initiateSupplierReturn(int returnQty, List<String> unitIdentifiers) {
        int toReturn = returnQty > 0 ? returnQty : this.damagedQuantity;
        this.quantity -= toReturn;
        if (this.damagedQuantity >= toReturn) {
            this.damagedQuantity -= toReturn;
        }
        updateDamageRecordStatuses(unitIdentifiers, DamageStatus.RETURNED_TO_SUPPLIER, toReturn);
        addMovement(MovementType.RETURNED, toReturn, null, primaryFulfillmentCenter, "Return to supplier initiated");
        this.setLastModifiedTime(new java.util.Date());
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
        this.setLastModifiedTime(new java.util.Date());
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
        this.setLastModifiedTime(new java.util.Date());
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

    /**
     * Returns only REPORTED damage records (active, unresolved damage).
     */
    public List<DamageRecord> getReportedDamageRecords() {
        return this.damageRecords.stream()
            .filter(r -> r.status() == DamageStatus.REPORTED)
            .toList();
    }

    /**
     * Updates damage record statuses. If unitIdentifiers provided, matches by unit ID.
     * Otherwise picks the first N REPORTED records.
     */
    private void updateDamageRecordStatuses(List<String> unitIdentifiers, DamageStatus newStatus, int count) {
        if (unitIdentifiers != null && !unitIdentifiers.isEmpty()) {
            for (String unitId : unitIdentifiers) {
                for (int i = 0; i < this.damageRecords.size(); i++) {
                    DamageRecord r = this.damageRecords.get(i);
                    if (r.status() == DamageStatus.REPORTED && unitId.equals(r.unitIdentifier())) {
                        this.damageRecords.set(i, new DamageRecord(
                            r.unitIdentifier(), r.location(), r.damageType(),
                            r.description(), r.discoveredAt(), newStatus));
                        break;
                    }
                }
            }
        } else {
            int updated = 0;
            for (int i = 0; i < this.damageRecords.size() && updated < count; i++) {
                DamageRecord r = this.damageRecords.get(i);
                if (r.status() == DamageStatus.REPORTED) {
                    this.damageRecords.set(i, new DamageRecord(
                        r.unitIdentifier(), r.location(), r.damageType(),
                        r.description(), r.discoveredAt(), newStatus));
                    updated++;
                }
            }
        }
    }

    private void addMovement(MovementType type, int qty, String refId, String fcId, String reason) {
        StockMovement movement = new StockMovement(type, qty, refId, fcId, Instant.now(), reason);
        this.movementHistory.add(movement);
    }

    @Override
    public TransientMap getTransientMap() { return transientMap; }

    public List<ActivityLog> getActivities() { return activities; }
    public void setActivities(List<ActivityLog> activities) { this.activities = activities; }

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

    public String getTenant() { return tenant; }
    public void setTenant(String tenant) { this.tenant = tenant; }
}
