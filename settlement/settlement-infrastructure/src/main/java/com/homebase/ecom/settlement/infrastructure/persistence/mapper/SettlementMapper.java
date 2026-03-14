package com.homebase.ecom.settlement.infrastructure.persistence.mapper;

import com.homebase.ecom.settlement.model.Settlement;
import com.homebase.ecom.settlement.model.SettlementActivityLog;
import com.homebase.ecom.settlement.model.SettlementLineItem;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementActivityLogEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementEntity;
import com.homebase.ecom.settlement.infrastructure.persistence.entity.SettlementLineItemEntity;
import java.util.stream.Collectors;

public class SettlementMapper {

    public SettlementEntity toEntity(Settlement model) {
        if (model == null) return null;
        SettlementEntity entity = new SettlementEntity();
        entity.setId(model.getId());
        entity.setDescription(model.getDescription());
        entity.setSupplierId(model.getSupplierId());
        entity.setPeriodMonth(model.getPeriodMonth());
        entity.setPeriodYear(model.getPeriodYear());
        entity.setTotalSalesAmount(model.getTotalSalesAmount());
        entity.setCommissionAmount(model.getCommissionAmount());
        entity.setNetPayoutAmount(model.getNetPayoutAmount());
        entity.setCurrentState(model.getCurrentState());

        if (model.getLineItems() != null) {
            entity.setLineItems(model.getLineItems().stream()
                    .map(item -> toLineItemEntity(item, entity))
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
        model.setPeriodMonth(entity.getPeriodMonth());
        model.setPeriodYear(entity.getPeriodYear());
        model.setTotalSalesAmount(entity.getTotalSalesAmount());
        model.setCommissionAmount(entity.getCommissionAmount());
        model.setNetPayoutAmount(entity.getNetPayoutAmount());
        model.setCurrentState(entity.getCurrentState());

        if (entity.getLineItems() != null) {
            model.setLineItems(entity.getLineItems().stream()
                    .map(this::toLineItemModel)
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

    private SettlementLineItemEntity toLineItemEntity(SettlementLineItem model, SettlementEntity settlementEntity) {
        if (model == null) return null;
        SettlementLineItemEntity entity = new SettlementLineItemEntity();
        entity.setId(model.getId());
        entity.setSettlement(settlementEntity);
        entity.setOrderId(model.getOrderId());
        entity.setOrderItemId(model.getOrderItemId());
        entity.setItemSalesAmount(model.getItemSalesAmount());
        entity.setItemCommissionAmount(model.getItemCommissionAmount());
        return entity;
    }

    private SettlementLineItem toLineItemModel(SettlementLineItemEntity entity) {
        if (entity == null) return null;
        SettlementLineItem model = new SettlementLineItem();
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setOrderItemId(entity.getOrderItemId());
        model.setItemSalesAmount(entity.getItemSalesAmount());
        model.setItemCommissionAmount(entity.getItemCommissionAmount());
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
