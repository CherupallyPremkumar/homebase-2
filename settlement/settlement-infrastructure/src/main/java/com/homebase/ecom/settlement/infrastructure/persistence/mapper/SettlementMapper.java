package com.homebase.ecom.settlement.infrastructure.persistence.mapper;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.model.SettlementActivityLog;
import com.homebase.ecom.settlement.model.SettlementAdjustment;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementActivityLogEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementAdjustmentEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import java.util.stream.Collectors;

public class SettlementMapper {

    public SettlementEntity toEntity(Settlement model) {
        if (model == null) return null;
        SettlementEntity entity = new SettlementEntity();
        entity.setId(model.getId());
        entity.setDescription(model.getDescription());
        entity.setSupplierId(model.getSupplierId());
        entity.setOrderId(model.getOrderId());
        entity.setOrderAmount(model.getOrderAmount());
        entity.setCommissionAmount(model.getCommissionAmount());
        entity.setPlatformFee(model.getPlatformFee());
        entity.setNetAmount(model.getNetAmount());
        entity.setSettlementPeriodStart(model.getSettlementPeriodStart());
        entity.setSettlementPeriodEnd(model.getSettlementPeriodEnd());
        entity.setDisbursementReference(model.getDisbursementReference());
        entity.setCurrentState(model.getCurrentState());

        if (model.getAdjustments() != null) {
            entity.setAdjustments(model.getAdjustments().stream()
                    .map(this::toAdjustmentEntity)
                    .collect(Collectors.toList()));
        }

        if (model.obtainActivities() != null) {
            entity.setActivities(model.obtainActivities().stream()
                    .map(activityLog -> toActivityLogEntity((SettlementActivityLog) activityLog))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public Settlement toModel(SettlementEntity entity) {
        if (entity == null) return null;
        Settlement model = new Settlement();
        model.setId(entity.getId());
        model.setDescription(entity.getDescription());
        model.setSupplierId(entity.getSupplierId());
        model.setOrderId(entity.getOrderId());
        model.setOrderAmount(entity.getOrderAmount());
        model.setCommissionAmount(entity.getCommissionAmount());
        model.setPlatformFee(entity.getPlatformFee());
        model.setNetAmount(entity.getNetAmount());
        model.setSettlementPeriodStart(entity.getSettlementPeriodStart());
        model.setSettlementPeriodEnd(entity.getSettlementPeriodEnd());
        model.setDisbursementReference(entity.getDisbursementReference());
        model.setCurrentState(entity.getCurrentState());

        if (entity.getAdjustments() != null) {
            model.setAdjustments(entity.getAdjustments().stream()
                    .map(this::toAdjustmentModel)
                    .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            entity.getActivities().forEach(activityLogEntity -> {
                SettlementActivityLog activityLog = toActivityLogModel(activityLogEntity);
                model.addActivity(activityLog.activityName, activityLog.activityComment);
            });
        }

        return model;
    }

    private SettlementAdjustmentEntity toAdjustmentEntity(SettlementAdjustment model) {
        if (model == null) return null;
        SettlementAdjustmentEntity entity = new SettlementAdjustmentEntity();
        entity.setId(model.getId());
        entity.setAmount(model.getAmount());
        entity.setReason(model.getReason());
        entity.setAdjustedBy(model.getAdjustedBy());
        entity.setAdjustedAt(model.getAdjustedAt());
        return entity;
    }

    private SettlementAdjustment toAdjustmentModel(SettlementAdjustmentEntity entity) {
        if (entity == null) return null;
        SettlementAdjustment model = new SettlementAdjustment();
        model.setId(entity.getId());
        model.setAmount(entity.getAmount());
        model.setReason(entity.getReason());
        model.setAdjustedBy(entity.getAdjustedBy());
        model.setAdjustedAt(entity.getAdjustedAt());
        return model;
    }

    private SettlementActivityLogEntity toActivityLogEntity(SettlementActivityLog model) {
        if (model == null) return null;
        SettlementActivityLogEntity entity = new SettlementActivityLogEntity();
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private SettlementActivityLog toActivityLogModel(SettlementActivityLogEntity entity) {
        if (entity == null) return null;
        SettlementActivityLog model = new SettlementActivityLog();
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
