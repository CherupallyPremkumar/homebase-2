package com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaActivityLogEntity;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaEntity;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSagaActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Maps between ReturnProcessingSaga domain model and ReturnProcessingSagaEntity JPA entity.
 * Covers ALL columns from return_processing_saga table (DB migration is source of truth).
 *
 * Wired as @Bean in ReturnProcessingConfiguration, NOT @Component.
 */
public class ReturnProcessingSagaMapper {

    public ReturnProcessingSagaEntity toEntity(ReturnProcessingSaga model) {
        if (model == null) return null;
        ReturnProcessingSagaEntity entity = new ReturnProcessingSagaEntity();

        // BaseJpaEntity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.tenant = model.getTenant();
        entity.setCreatedBy(model.getCreatedBy());
        if (model.getVersion() != null) entity.setVersion(model.getVersion());

        // AbstractJpaStateEntity / STM fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Business fields
        entity.setReturnRequestId(model.getReturnRequestId());
        entity.setOrderId(model.getOrderId());
        entity.setOrderItemId(model.getOrderItemId());
        entity.setRefundAmount(model.getRefundAmount());
        entity.setShipmentId(model.getShipmentId());
        entity.setSettlementAdjustmentId(model.getSettlementAdjustmentId());
        entity.setRefundId(model.getRefundId());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());

        // Activity logs
        if (model.getActivities() != null) {
            entity.setActivities(model.getActivities().stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        } else {
            entity.setActivities(new ArrayList<>());
        }
        return entity;
    }

    public ReturnProcessingSaga toModel(ReturnProcessingSagaEntity entity) {
        if (entity == null) return null;
        ReturnProcessingSaga model = new ReturnProcessingSaga();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setTenant(entity.tenant);
        model.setCreatedBy(entity.getCreatedBy());
        if (entity.getVersion() != null) model.setVersion(entity.getVersion());

        // STM fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Business fields
        model.setReturnRequestId(entity.getReturnRequestId());
        model.setOrderId(entity.getOrderId());
        model.setOrderItemId(entity.getOrderItemId());
        model.setRefundAmount(entity.getRefundAmount());
        model.setShipmentId(entity.getShipmentId());
        model.setSettlementAdjustmentId(entity.getSettlementAdjustmentId());
        model.setRefundId(entity.getRefundId());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());

        // Activity logs
        if (entity.getActivities() != null) {
            model.setActivities(entity.getActivities().stream()
                    .map(this::toActivityModel)
                    .collect(Collectors.toList()));
        } else {
            model.setActivities(new ArrayList<>());
        }
        return model;
    }

    /**
     * Merges incoming (partial) domain model from the request onto the existing persisted entity.
     * Only overwrites non-null business fields — STM/base fields are managed by Chenile.
     */
    public ReturnProcessingSaga mergeEntity(ReturnProcessingSaga incoming, ReturnProcessingSaga existing) {
        if (incoming == null) return existing;
        if (existing == null) return incoming;

        if (incoming.getReturnRequestId() != null) existing.setReturnRequestId(incoming.getReturnRequestId());
        if (incoming.getOrderId() != null) existing.setOrderId(incoming.getOrderId());
        if (incoming.getOrderItemId() != null) existing.setOrderItemId(incoming.getOrderItemId());
        if (incoming.getRefundAmount() != null) existing.setRefundAmount(incoming.getRefundAmount());
        if (incoming.getShipmentId() != null) existing.setShipmentId(incoming.getShipmentId());
        if (incoming.getSettlementAdjustmentId() != null) existing.setSettlementAdjustmentId(incoming.getSettlementAdjustmentId());
        if (incoming.getRefundId() != null) existing.setRefundId(incoming.getRefundId());
        if (incoming.getErrorMessage() != null) existing.setErrorMessage(incoming.getErrorMessage());
        if (incoming.getRetryCount() > 0) existing.setRetryCount(incoming.getRetryCount());
        if (incoming.getTenant() != null) existing.setTenant(incoming.getTenant());

        return existing;
    }

    /**
     * Merges incoming JPA entity fields onto an existing managed JPA entity.
     * Used by ChenileJpaEntityStore on the update path.
     * Copies STM state + business fields from incoming onto existing.
     */
    public void mergeJpaEntity(ReturnProcessingSagaEntity existing, ReturnProcessingSagaEntity incoming) {
        if (existing == null || incoming == null) return;

        // STM fields managed by Chenile
        existing.setCurrentState(incoming.getCurrentState());
        existing.setStateEntryTime(incoming.getStateEntryTime());
        existing.setSlaTendingLate(incoming.getSlaTendingLate());
        existing.setSlaLate(incoming.getSlaLate());

        // Business fields
        if (incoming.getReturnRequestId() != null) existing.setReturnRequestId(incoming.getReturnRequestId());
        if (incoming.getOrderId() != null) existing.setOrderId(incoming.getOrderId());
        if (incoming.getOrderItemId() != null) existing.setOrderItemId(incoming.getOrderItemId());
        if (incoming.getRefundAmount() != null) existing.setRefundAmount(incoming.getRefundAmount());
        if (incoming.getShipmentId() != null) existing.setShipmentId(incoming.getShipmentId());
        if (incoming.getSettlementAdjustmentId() != null) existing.setSettlementAdjustmentId(incoming.getSettlementAdjustmentId());
        if (incoming.getRefundId() != null) existing.setRefundId(incoming.getRefundId());
        existing.setErrorMessage(incoming.getErrorMessage());
        existing.setRetryCount(incoming.getRetryCount());

        // Activity logs
        if (incoming.getActivities() != null) {
            existing.setActivities(incoming.getActivities());
        }
    }

    private ReturnProcessingSagaActivityLogEntity toActivityEntity(ReturnProcessingSagaActivityLog model) {
        if (model == null) return null;
        ReturnProcessingSagaActivityLogEntity entity = new ReturnProcessingSagaActivityLogEntity();
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private ReturnProcessingSagaActivityLog toActivityModel(ReturnProcessingSagaActivityLogEntity entity) {
        if (entity == null) return null;
        ReturnProcessingSagaActivityLog model = new ReturnProcessingSagaActivityLog();
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
