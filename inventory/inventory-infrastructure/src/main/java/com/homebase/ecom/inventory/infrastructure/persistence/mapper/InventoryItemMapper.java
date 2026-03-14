package com.homebase.ecom.inventory.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.homebase.ecom.inventory.domain.model.InventoryItem;
import com.homebase.ecom.inventory.domain.model.Reservation;
import com.homebase.ecom.inventory.domain.model.StockMovement;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryItemEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.InventoryReservationEntity;
import com.homebase.ecom.inventory.infrastructure.persistence.entity.StockMovementEntity;

public class InventoryItemMapper {

    public InventoryItem toModel(InventoryItemEntity entity) {
        if (entity == null) return null;
        InventoryItem model = new InventoryItem();
        model.setId(entity.getId());
        model.setSku(entity.getSku());
        model.setAsin(entity.getAsin());
        model.setProductId(entity.getProductId());
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
        if (entity.getCreatedTime() != null) model.setCreatedAt(entity.getCreatedTime().toInstant());
        if (entity.getLastModifiedTime() != null) model.setUpdatedAt(entity.getLastModifiedTime().toInstant());
        // For STM state
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

        return model;
    }

    public InventoryItemEntity toEntity(InventoryItem model) {
        if (model == null) return null;
        InventoryItemEntity entity = new InventoryItemEntity();
        entity.setId(model.getId());
        entity.setSku(model.getSku());
        entity.setAsin(model.getAsin());
        entity.setProductId(model.getProductId());
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
        // For STM state
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
}
