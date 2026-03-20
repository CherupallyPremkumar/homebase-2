package com.homebase.ecom.inventory.infrastructure.persistence.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.chenile.jpautils.entity.AbstractJpaStateEntity;
import com.homebase.ecom.inventory.domain.model.InventoryStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory_item")
public class InventoryItemEntity extends AbstractJpaStateEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "sku")
    private String sku;

    @Column(name = "asin")
    private String asin;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "variant_id", unique = true)
    private String variantId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "available_quantity")
    private Integer availableQuantity;

    @Column(name = "reserved_quantity")
    private Integer reservedQuantity;

    @Column(name = "inbound_quantity")
    private Integer inboundQuantity;

    @Column(name = "damaged_quantity")
    private Integer damagedQuantity;

    @Column(name = "primary_fc")
    private String primaryFulfillmentCenter;

    @Column(name = "is_fba")
    private Boolean isFbaEnabled;

    @Column(name = "is_merchant_fulfilled")
    private Boolean isMerchantFulfilled;

    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold;

    @Column(name = "out_of_stock_threshold")
    private Integer outOfStockThreshold;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InventoryStatus status;

    @Column(name = "supplier_id")
    private String supplierId;

    @Column(name = "cost_price", precision = 12, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "batch_number", length = 100)
    private String batchNumber;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "shelf_location", length = 100)
    private String shelfLocation;

    @ElementCollection
    @CollectionTable(name = "inventory_reservations", joinColumns = @JoinColumn(name = "inventory_item_id"))
    private List<InventoryReservationEntity> activeReservations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "inventory_movements", joinColumns = @JoinColumn(name = "inventory_item_id"))
    private List<StockMovementEntity> movementHistory = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "inventory_damage_records", joinColumns = @JoinColumn(name = "inventory_item_id"))
    private List<DamageRecordEntity> damageRecords = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "inventory_item_id")
    private List<InventoryItemActivityLogEntity> activities = new ArrayList<>();

    @Column(name = "last_sale_at")
    private Instant lastSaleAt;

    @Column(name = "last_restock_at")
    private Instant lastRestockAt;

    // Getters and Setters
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

    public InventoryStatus getStatus() { return status; }
    public void setStatus(InventoryStatus status) { this.status = status; }

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

    public List<InventoryReservationEntity> getActiveReservations() { return activeReservations; }
    public void setActiveReservations(List<InventoryReservationEntity> activeReservations) { this.activeReservations = activeReservations; }

    public List<StockMovementEntity> getMovementHistory() { return movementHistory; }
    public void setMovementHistory(List<StockMovementEntity> movementHistory) { this.movementHistory = movementHistory; }

    public List<DamageRecordEntity> getDamageRecords() { return damageRecords; }
    public void setDamageRecords(List<DamageRecordEntity> damageRecords) { this.damageRecords = damageRecords; }

    public List<InventoryItemActivityLogEntity> getActivities() { return activities; }
    public void setActivities(List<InventoryItemActivityLogEntity> activities) { this.activities = activities; }

    public Instant getLastSaleAt() { return lastSaleAt; }
    public void setLastSaleAt(Instant lastSaleAt) { this.lastSaleAt = lastSaleAt; }

    public Instant getLastRestockAt() { return lastRestockAt; }
    public void setLastRestockAt(Instant lastRestockAt) { this.lastRestockAt = lastRestockAt; }
}
