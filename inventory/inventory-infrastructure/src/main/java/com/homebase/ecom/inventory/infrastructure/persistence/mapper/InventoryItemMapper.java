package com.homebase.ecom.inventory.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.homebase.ecom.inventory.domain.model.DamageRecord;
import com.homebase.ecom.inventory.domain.model.InventoryActivityLog;
import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.Reservation;
import com.homebase.ecom.inventory.domain.model.StockMovement;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.DamageRecordEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemActivityLogEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryReservationEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.StockMovementEntity;
import org.chenile.workflow.activities.model.ActivityLog;

public class InventoryItemMapper {

    public InventoryItem toModel(InventoryItemEntity entity) {
        if (entity == null) return null;
        InventoryItem model = new InventoryItem();

        // BaseEntity fields
        model.setId(entity.getId());
        if (entity.getCreatedTime() != null) model.setCreatedTime(entity.getCreatedTime());
        if (entity.getLastModifiedTime() != null) model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion());

        // STM state fields
        model.setCurrentState(entity.getCurrentState());
        if (entity.getStateEntryTime() != null) model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Tenant
        model.setTenant(entity.tenant);

        // Core identity
        model.setDescription(entity.getDescription());
        model.setSku(entity.getSku());
        model.setAsin(entity.getAsin());
        model.setProductId(entity.getProductId());
        model.setVariantId(entity.getVariantId());

        // Stock quantities
        model.setQuantity(entity.getQuantity() != null ? entity.getQuantity() : 0);
        model.setAvailableQuantity(entity.getAvailableQuantity() != null ? entity.getAvailableQuantity() : 0);
        model.setReservedQuantity(entity.getReservedQuantity() != null ? entity.getReservedQuantity() : 0);
        model.setInboundQuantity(entity.getInboundQuantity() != null ? entity.getInboundQuantity() : 0);
        model.setDamagedQuantity(entity.getDamagedQuantity() != null ? entity.getDamagedQuantity() : 0);

        // Fulfillment configuration
        model.setPrimaryFulfillmentCenter(entity.getPrimaryFulfillmentCenter());
        model.setIsFbaEnabled(entity.getIsFbaEnabled() != null ? entity.getIsFbaEnabled() : false);
        model.setIsMerchantFulfilled(entity.getIsMerchantFulfilled() != null ? entity.getIsMerchantFulfilled() : true);

        // Thresholds & status
        model.setLowStockThreshold(entity.getLowStockThreshold() != null ? entity.getLowStockThreshold() : 10);
        model.setOutOfStockThreshold(entity.getOutOfStockThreshold() != null ? entity.getOutOfStockThreshold() : 0);
        model.setStatus(entity.getStatus());

        // Supply chain fields (inventory-006 migration)
        model.setSupplierId(entity.getSupplierId());
        model.setCostPrice(entity.getCostPrice());
        model.setBatchNumber(entity.getBatchNumber());
        model.setExpiryDate(entity.getExpiryDate());
        model.setShelfLocation(entity.getShelfLocation());

        // Timestamps
        model.setLastSaleAt(entity.getLastSaleAt());
        model.setLastRestockAt(entity.getLastRestockAt());

        // Collections
        if (entity.getActiveReservations() != null) {
            model.setActiveReservations(entity.getActiveReservations().stream()
                .map(this::toReservationModel)
                .collect(Collectors.toList()));
        }

        if (entity.getMovementHistory() != null) {
            model.setMovementHistory(entity.getMovementHistory().stream()
                .map(this::toMovementModel)
                .collect(Collectors.toList()));
        }

        if (entity.getDamageRecords() != null) {
            model.setDamageRecords(entity.getDamageRecords().stream()
                .map(this::toDamageRecordModel)
                .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                .map(this::toActivityModel)
                .collect(Collectors.toList()));
        }

        return model;
    }

    public InventoryItemEntity toEntity(InventoryItem model) {
        if (model == null) return null;
        InventoryItemEntity entity = new InventoryItemEntity();

        // BaseEntity fields
        entity.setId(model.getId());
        if (model.getCreatedTime() != null) entity.setCreatedTime(model.getCreatedTime());
        if (model.getLastModifiedTime() != null) entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
        if (model.getVersion() != null) entity.setVersion(model.getVersion());

        // STM state fields
        entity.setCurrentState(model.getCurrentState());
        if (model.getStateEntryTime() != null) entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Tenant
        entity.tenant = model.getTenant();

        // Core identity
        entity.setDescription(model.getDescription());
        entity.setSku(model.getSku());
        entity.setAsin(model.getAsin());
        entity.setProductId(model.getProductId());
        entity.setVariantId(model.getVariantId());

        // Stock quantities
        entity.setQuantity(model.getQuantity());
        entity.setAvailableQuantity(model.getAvailableQuantity());
        entity.setReservedQuantity(model.getReservedQuantity());
        entity.setInboundQuantity(model.getInboundQuantity());
        entity.setDamagedQuantity(model.getDamagedQuantity());

        // Fulfillment configuration
        entity.setPrimaryFulfillmentCenter(model.getPrimaryFulfillmentCenter());
        entity.setIsFbaEnabled(model.getIsFbaEnabled());
        entity.setIsMerchantFulfilled(model.getIsMerchantFulfilled());

        // Thresholds & status
        entity.setLowStockThreshold(model.getLowStockThreshold());
        entity.setOutOfStockThreshold(model.getOutOfStockThreshold());
        entity.setStatus(model.getStatus());

        // Supply chain fields (inventory-006 migration)
        entity.setSupplierId(model.getSupplierId());
        entity.setCostPrice(model.getCostPrice());
        entity.setBatchNumber(model.getBatchNumber());
        entity.setExpiryDate(model.getExpiryDate());
        entity.setShelfLocation(model.getShelfLocation());

        // Timestamps
        entity.setLastSaleAt(model.getLastSaleAt());
        entity.setLastRestockAt(model.getLastRestockAt());

        // Collections
        if (model.getActiveReservations() != null) {
            entity.setActiveReservations(model.getActiveReservations().stream()
                .map(this::toReservationEntity)
                .collect(Collectors.toList()));
        }

        if (model.getMovementHistory() != null) {
            entity.setMovementHistory(model.getMovementHistory().stream()
                .map(this::toMovementEntity)
                .collect(Collectors.toList()));
        }

        if (model.getDamageRecords() != null) {
            entity.setDamageRecords(model.getDamageRecords().stream()
                .map(this::toDamageRecordEntity)
                .collect(Collectors.toList()));
        }

        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList()));
        }

        return entity;
    }

    /**
     * Merges incoming domain model fields onto an existing JPA entity loaded from the database.
     * Preserves DB-managed fields (id, createdTime, createdBy, version) from the existing entity
     * and applies mutable fields from the incoming model.
     */
    public InventoryItemEntity mergeEntity(InventoryItem incoming, InventoryItemEntity existing) {
        if (incoming == null || existing == null) return existing;

        // STM state fields
        if (incoming.getCurrentState() != null) existing.setCurrentState(incoming.getCurrentState());
        if (incoming.getStateEntryTime() != null) existing.setStateEntryTime(incoming.getStateEntryTime());
        existing.setSlaTendingLate(incoming.getSlaTendingLate());
        existing.setSlaLate(incoming.getSlaLate());

        // Mutable identity fields
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        if (incoming.getSku() != null) existing.setSku(incoming.getSku());
        if (incoming.getAsin() != null) existing.setAsin(incoming.getAsin());
        if (incoming.getProductId() != null) existing.setProductId(incoming.getProductId());
        if (incoming.getVariantId() != null) existing.setVariantId(incoming.getVariantId());

        // Stock quantities (always overwrite -- business logic has already computed)
        existing.setQuantity(incoming.getQuantity());
        existing.setAvailableQuantity(incoming.getAvailableQuantity());
        existing.setReservedQuantity(incoming.getReservedQuantity());
        existing.setInboundQuantity(incoming.getInboundQuantity());
        existing.setDamagedQuantity(incoming.getDamagedQuantity());

        // Fulfillment configuration
        if (incoming.getPrimaryFulfillmentCenter() != null) existing.setPrimaryFulfillmentCenter(incoming.getPrimaryFulfillmentCenter());
        existing.setIsFbaEnabled(incoming.getIsFbaEnabled());
        existing.setIsMerchantFulfilled(incoming.getIsMerchantFulfilled());

        // Thresholds & status
        existing.setLowStockThreshold(incoming.getLowStockThreshold());
        existing.setOutOfStockThreshold(incoming.getOutOfStockThreshold());
        existing.setStatus(incoming.getStatus());

        // Supply chain fields
        if (incoming.getSupplierId() != null) existing.setSupplierId(incoming.getSupplierId());
        if (incoming.getCostPrice() != null) existing.setCostPrice(incoming.getCostPrice());
        if (incoming.getBatchNumber() != null) existing.setBatchNumber(incoming.getBatchNumber());
        if (incoming.getExpiryDate() != null) existing.setExpiryDate(incoming.getExpiryDate());
        if (incoming.getShelfLocation() != null) existing.setShelfLocation(incoming.getShelfLocation());

        // Timestamps
        existing.setLastSaleAt(incoming.getLastSaleAt());
        existing.setLastRestockAt(incoming.getLastRestockAt());
        if (incoming.getLastModifiedTime() != null) existing.setLastModifiedTime(incoming.getLastModifiedTime());
        if (incoming.getLastModifiedBy() != null) existing.setLastModifiedBy(incoming.getLastModifiedBy());

        // Collections -- replace with incoming state
        if (incoming.getActiveReservations() != null) {
            existing.getActiveReservations().clear();
            existing.getActiveReservations().addAll(incoming.getActiveReservations().stream()
                .map(this::toReservationEntity)
                .collect(Collectors.toList()));
        }

        if (incoming.getMovementHistory() != null) {
            existing.getMovementHistory().clear();
            existing.getMovementHistory().addAll(incoming.getMovementHistory().stream()
                .map(this::toMovementEntity)
                .collect(Collectors.toList()));
        }

        if (incoming.getDamageRecords() != null) {
            existing.getDamageRecords().clear();
            existing.getDamageRecords().addAll(incoming.getDamageRecords().stream()
                .map(this::toDamageRecordEntity)
                .collect(Collectors.toList()));
        }

        if (incoming.getActivities() != null) {
            existing.getActivities().clear();
            existing.getActivities().addAll(incoming.getActivities().stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList()));
        }

        return existing;
    }

    private Reservation toReservationModel(InventoryReservationEntity entity) {
        return new Reservation(entity.getOrderId(), entity.getSessionId(), entity.getQuantity(),
            entity.getReservedAt(), entity.getExpiresAt(), entity.getStatus());
    }

    private InventoryReservationEntity toReservationEntity(Reservation model) {
        InventoryReservationEntity entity = new InventoryReservationEntity();
        entity.setOrderId(model.orderId());
        entity.setSessionId(model.sessionId());
        entity.setQuantity(model.quantity());
        entity.setReservedAt(model.reservedAt());
        entity.setExpiresAt(model.expiresAt());
        entity.setStatus(model.status());
        return entity;
    }

    private StockMovement toMovementModel(StockMovementEntity entity) {
        return new StockMovement(entity.getType(), entity.getQuantity(), entity.getReferenceId(),
            entity.getFulfillmentCenterId(), entity.getMovementTime(), entity.getReason());
    }

    private StockMovementEntity toMovementEntity(StockMovement model) {
        StockMovementEntity entity = new StockMovementEntity();
        entity.setType(model.type());
        entity.setQuantity(model.quantity());
        entity.setReferenceId(model.referenceId());
        entity.setFulfillmentCenterId(model.fulfillmentCenterId());
        entity.setMovementTime(model.movementTime());
        entity.setReason(model.reason());
        return entity;
    }

    private DamageRecord toDamageRecordModel(DamageRecordEntity entity) {
        return new DamageRecord(entity.getUnitIdentifier(), entity.getLocation(),
            entity.getDamageType(), entity.getDescription(),
            entity.getDiscoveredAt(), entity.getStatus());
    }

    private DamageRecordEntity toDamageRecordEntity(DamageRecord model) {
        DamageRecordEntity entity = new DamageRecordEntity();
        entity.setUnitIdentifier(model.unitIdentifier());
        entity.setLocation(model.location());
        entity.setDamageType(model.damageType());
        entity.setDescription(model.description());
        entity.setDiscoveredAt(model.discoveredAt());
        entity.setStatus(model.status());
        return entity;
    }

    private ActivityLog toActivityModel(InventoryItemActivityLogEntity entity) {
        InventoryActivityLog log = new InventoryActivityLog();
        log.activityName = entity.getName();
        log.activityComment = entity.getComment();
        log.activitySuccess = entity.getSuccess();
        return log;
    }

    private InventoryItemActivityLogEntity toActivityEntity(ActivityLog model) {
        InventoryItemActivityLogEntity entity = new InventoryItemActivityLogEntity();
        entity.setEventId(model.getName());
        entity.setComment(model.getComment());
        entity.setSuccess(model.getSuccess());
        return entity;
    }
}
