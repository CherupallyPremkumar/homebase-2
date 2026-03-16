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
        model.setId(entity.getId());
        model.setDescription(entity.getDescription());
        model.setSku(entity.getSku());
        model.setAsin(entity.getAsin());
        model.setProductId(entity.getProductId());
        model.setVariantId(entity.getVariantId());
        model.setQuantity(entity.getQuantity());
        model.setAvailableQuantity(entity.getAvailableQuantity());
        model.setReservedQuantity(entity.getReservedQuantity());
        model.setInboundQuantity(entity.getInboundQuantity());
        model.setDamagedQuantity(entity.getDamagedQuantity());
        model.setPrimaryFulfillmentCenter(entity.getPrimaryFulfillmentCenter());
        model.setIsFbaEnabled(entity.getIsFbaEnabled());
        model.setIsMerchantFulfilled(entity.getIsMerchantFulfilled());
        model.setLowStockThreshold(entity.getLowStockThreshold());
        model.setOutOfStockThreshold(entity.getOutOfStockThreshold());
        model.setStatus(entity.getStatus());
        model.setLastSaleAt(entity.getLastSaleAt());
        model.setLastRestockAt(entity.getLastRestockAt());
        if (entity.getCreatedTime() != null) model.setCreatedTime(entity.getCreatedTime());
        if (entity.getLastModifiedTime() != null) model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCurrentState(entity.getCurrentState());

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
        entity.setId(model.getId());
        entity.setDescription(model.getDescription());
        entity.setSku(model.getSku());
        entity.setAsin(model.getAsin());
        entity.setProductId(model.getProductId());
        entity.setVariantId(model.getVariantId());
        entity.setQuantity(model.getQuantity());
        entity.setAvailableQuantity(model.getAvailableQuantity());
        entity.setReservedQuantity(model.getReservedQuantity());
        entity.setInboundQuantity(model.getInboundQuantity());
        entity.setDamagedQuantity(model.getDamagedQuantity());
        entity.setPrimaryFulfillmentCenter(model.getPrimaryFulfillmentCenter());
        entity.setIsFbaEnabled(model.getIsFbaEnabled());
        entity.setIsMerchantFulfilled(model.getIsMerchantFulfilled());
        entity.setLowStockThreshold(model.getLowStockThreshold());
        entity.setOutOfStockThreshold(model.getOutOfStockThreshold());
        entity.setStatus(model.getStatus());
        entity.setLastSaleAt(model.getLastSaleAt());
        entity.setLastRestockAt(model.getLastRestockAt());
        entity.setCurrentState(model.getCurrentState());

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
