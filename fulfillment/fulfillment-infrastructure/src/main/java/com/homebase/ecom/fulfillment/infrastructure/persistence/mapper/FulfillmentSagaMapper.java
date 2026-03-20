package com.homebase.ecom.fulfillment.infrastructure.persistence.mapper;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.model.FulfillmentSagaActivityLog;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaActivityLogEntity;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaEntity;

import java.util.ArrayList;
import java.util.List;

public class FulfillmentSagaMapper {

    public FulfillmentSaga toModel(FulfillmentSagaEntity entity) {
        if (entity == null) return null;
        FulfillmentSaga model = new FulfillmentSaga();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.createdBy);
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setVersion(entity.getVersion());

        // AbstractJpaStateEntity / state fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Fulfillment-specific fields
        model.setOrderId(entity.getOrderId());
        model.setUserId(entity.getUserId());
        model.setShipmentId(entity.getShipmentId());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        model.setTenant(entity.tenant);

        // Activities
        if (entity.getActivities() != null) {
            List<FulfillmentSagaActivityLog> activityLogs = new ArrayList<>();
            for (FulfillmentSagaActivityLogEntity actEntity : entity.getActivities()) {
                FulfillmentSagaActivityLog log = new FulfillmentSagaActivityLog();
                log.activityName = actEntity.activityName;
                log.activitySuccess = actEntity.activitySuccess;
                log.activityComment = actEntity.activityComment;
                activityLogs.add(log);
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public FulfillmentSagaEntity toEntity(FulfillmentSaga model) {
        if (model == null) return null;
        FulfillmentSagaEntity entity = new FulfillmentSagaEntity();

        // BaseEntity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.createdBy = model.getCreatedBy();
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // State fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Fulfillment-specific fields
        entity.setOrderId(model.getOrderId());
        entity.setUserId(model.getUserId());
        entity.setShipmentId(model.getShipmentId());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        entity.tenant = model.getTenant();

        // Activities
        if (model.getActivities() != null) {
            ArrayList<FulfillmentSagaActivityLogEntity> actEntities = new ArrayList<>();
            for (FulfillmentSagaActivityLog act : model.getActivities()) {
                FulfillmentSagaActivityLogEntity actEntity = new FulfillmentSagaActivityLogEntity();
                actEntity.activityName = act.activityName;
                actEntity.activitySuccess = act.activitySuccess;
                actEntity.activityComment = act.activityComment;
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }

    /**
     * Merges incoming model fields onto existing entity for update operations.
     * Preserves base entity audit fields from existing while applying changes from incoming.
     */
    public FulfillmentSagaEntity mergeEntity(FulfillmentSaga incoming, FulfillmentSagaEntity existing) {
        if (incoming == null) return existing;
        if (existing == null) return toEntity(incoming);

        // State fields — always take from incoming (STM manages these)
        existing.setCurrentState(incoming.getCurrentState());
        existing.setStateEntryTime(incoming.getStateEntryTime());
        existing.setSlaTendingLate(incoming.getSlaTendingLate());
        existing.setSlaLate(incoming.getSlaLate());

        // Fulfillment-specific fields
        if (incoming.getOrderId() != null) existing.setOrderId(incoming.getOrderId());
        if (incoming.getUserId() != null) existing.setUserId(incoming.getUserId());
        existing.setShipmentId(incoming.getShipmentId());
        existing.setTrackingNumber(incoming.getTrackingNumber());
        existing.setErrorMessage(incoming.getErrorMessage());
        existing.setRetryCount(incoming.getRetryCount());
        if (incoming.getTenant() != null) existing.tenant = incoming.getTenant();

        // Activities — replace with incoming
        if (incoming.getActivities() != null) {
            ArrayList<FulfillmentSagaActivityLogEntity> actEntities = new ArrayList<>();
            for (FulfillmentSagaActivityLog act : incoming.getActivities()) {
                FulfillmentSagaActivityLogEntity actEntity = new FulfillmentSagaActivityLogEntity();
                actEntity.activityName = act.activityName;
                actEntity.activitySuccess = act.activitySuccess;
                actEntity.activityComment = act.activityComment;
                actEntities.add(actEntity);
            }
            existing.getActivities().clear();
            existing.getActivities().addAll(actEntities);
        }

        return existing;
    }
}
