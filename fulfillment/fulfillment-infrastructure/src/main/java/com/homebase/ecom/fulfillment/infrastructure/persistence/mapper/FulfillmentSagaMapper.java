package com.homebase.ecom.fulfillment.infrastructure.persistence.mapper;

import com.homebase.ecom.fulfillment.model.FulfillmentSaga;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaActivityLogEntity;
import com.homebase.ecom.fulfillment.infrastructure.persistence.entity.FulfillmentSagaEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;

public class FulfillmentSagaMapper {

    public FulfillmentSaga toModel(FulfillmentSagaEntity entity) {
        if (entity == null) return null;
        FulfillmentSaga model = new FulfillmentSaga();
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setUserId(entity.getUserId());
        model.setShipmentId(entity.getShipmentId());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);

        if (entity.getActivities() != null) {
            for (FulfillmentSagaActivityLogEntity actEntity : entity.getActivities()) {
                model.addActivity(actEntity.getName(), actEntity.getComment());
            }
        }

        return model;
    }

    public FulfillmentSagaEntity toEntity(FulfillmentSaga model) {
        if (model == null) return null;
        FulfillmentSagaEntity entity = new FulfillmentSagaEntity();
        entity.setId(model.getId());
        entity.setOrderId(model.getOrderId());
        entity.setUserId(model.getUserId());
        entity.setShipmentId(model.getShipmentId());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        if (model.obtainActivities() != null) {
            ArrayList<FulfillmentSagaActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog act : model.obtainActivities()) {
                FulfillmentSagaActivityLogEntity actEntity = new FulfillmentSagaActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }
}
