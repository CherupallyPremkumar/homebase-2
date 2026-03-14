package com.homebase.ecom.returnprocessing.infrastructure.persistence.mapper;

import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaActivityLogEntity;
import com.homebase.ecom.returnprocessing.infrastructure.persistence.entity.ReturnProcessingSagaEntity;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSaga;
import com.homebase.ecom.returnprocessing.model.ReturnProcessingSagaActivityLog;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ReturnProcessingSagaMapper {

    public ReturnProcessingSagaEntity toEntity(ReturnProcessingSaga model) {
        if (model == null) return null;
        ReturnProcessingSagaEntity entity = new ReturnProcessingSagaEntity();
        entity.setId(model.getId());
        entity.setReturnRequestId(model.getReturnRequestId());
        entity.setOrderId(model.getOrderId());
        entity.setOrderItemId(model.getOrderItemId());
        entity.setRefundAmount(model.getRefundAmount());
        entity.setShipmentId(model.getShipmentId());
        entity.setSettlementAdjustmentId(model.getSettlementAdjustmentId());
        entity.setRefundId(model.getRefundId());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());
        entity.setCurrentState(model.getCurrentState());

        if (model.activities != null) {
            entity.setActivities(model.activities.stream()
                    .map(this::toActivityEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    public ReturnProcessingSaga toModel(ReturnProcessingSagaEntity entity) {
        if (entity == null) return null;
        ReturnProcessingSaga model = new ReturnProcessingSaga();
        model.setId(entity.getId());
        model.setReturnRequestId(entity.getReturnRequestId());
        model.setOrderId(entity.getOrderId());
        model.setOrderItemId(entity.getOrderItemId());
        model.setRefundAmount(entity.getRefundAmount());
        model.setShipmentId(entity.getShipmentId());
        model.setSettlementAdjustmentId(entity.getSettlementAdjustmentId());
        model.setRefundId(entity.getRefundId());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());
        model.setCurrentState(entity.getCurrentState());

        if (entity.getActivities() != null) {
            model.activities = entity.getActivities().stream()
                    .map(this::toActivityModel)
                    .collect(Collectors.toList());
        }
        return model;
    }

    private ReturnProcessingSagaActivityLogEntity toActivityEntity(ReturnProcessingSagaActivityLog model) {
        if (model == null) return null;
        ReturnProcessingSagaActivityLogEntity entity = new ReturnProcessingSagaActivityLogEntity();
        entity.setId(model.getId());
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private ReturnProcessingSagaActivityLog toActivityModel(ReturnProcessingSagaActivityLogEntity entity) {
        if (entity == null) return null;
        ReturnProcessingSagaActivityLog model = new ReturnProcessingSagaActivityLog();
        model.setId(entity.getId());
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
