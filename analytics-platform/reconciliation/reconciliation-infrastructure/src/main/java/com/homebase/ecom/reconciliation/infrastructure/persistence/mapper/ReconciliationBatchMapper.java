package com.homebase.ecom.reconciliation.infrastructure.persistence.mapper;

import com.homebase.ecom.reconciliation.model.ReconciliationBatch;
import com.homebase.ecom.reconciliation.model.ReconciliationBatchActivityLog;
import com.homebase.ecom.reconciliation.infrastructure.persistence.entity.ReconciliationBatchEntity;
import com.homebase.ecom.reconciliation.infrastructure.persistence.entity.ReconciliationBatchActivityLogEntity;

import java.util.stream.Collectors;

/**
 * Bidirectional mapper between ReconciliationBatch domain model and ReconciliationBatchEntity JPA entity.
 * Maps ALL fields from the reconciliation_batches table. Null-safe on all conversions.
 */
public class ReconciliationBatchMapper {

    public ReconciliationBatchEntity toEntity(ReconciliationBatch model) {
        if (model == null) return null;
        ReconciliationBatchEntity entity = new ReconciliationBatchEntity();
        mapModelToEntity(model, entity);
        return entity;
    }

    public ReconciliationBatch toModel(ReconciliationBatchEntity entity) {
        if (entity == null) return null;
        ReconciliationBatch model = new ReconciliationBatch();

        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setCreatedBy(entity.getCreatedBy());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setVersion(entity.getVersion());
        model.setTenant(entity.tenant);

        // STM fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());

        // Business fields
        model.setBatchDate(entity.getBatchDate());
        model.setPeriodStart(entity.getPeriodStart());
        model.setPeriodEnd(entity.getPeriodEnd());
        model.setGatewayType(entity.getGatewayType());
        model.setReconciliationMethod(entity.getReconciliationMethod());
        model.setMatchedCount(entity.getMatchedCount());
        model.setMismatchCount(entity.getMismatchCount());
        model.setAutoResolvedCount(entity.getAutoResolvedCount());
        model.setUnresolvedCount(entity.getUnresolvedCount());
        model.setTotalGatewayAmount(entity.getTotalGatewayAmount());
        model.setTotalSystemAmount(entity.getTotalSystemAmount());
        model.setVarianceAmount(entity.getVarianceAmount());
        model.setCompletedAt(entity.getCompletedAt());
        model.setErrorMessage(entity.getErrorMessage());

        // Derived field for OGNL auto-state
        model.setUnresolvedMismatchCount(entity.getUnresolvedCount());

        // Activity log
        if (entity.getActivities() != null) {
            entity.getActivities().forEach(activityLogEntity -> {
                ReconciliationBatchActivityLog log = toActivityLogModel(activityLogEntity);
                model.addActivity(log.activityName, log.activityComment);
            });
        }

        return model;
    }

    /**
     * Merges updated JPA entity fields onto an existing JPA entity (for update scenarios).
     */
    public void mergeEntity(ReconciliationBatchEntity existing, ReconciliationBatchEntity updated) {
        if (updated == null || existing == null) return;

        // STM fields
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());

        // Business fields
        existing.setBatchDate(updated.getBatchDate());
        existing.setPeriodStart(updated.getPeriodStart());
        existing.setPeriodEnd(updated.getPeriodEnd());
        existing.setGatewayType(updated.getGatewayType());
        existing.setReconciliationMethod(updated.getReconciliationMethod());
        existing.setMatchedCount(updated.getMatchedCount());
        existing.setMismatchCount(updated.getMismatchCount());
        existing.setAutoResolvedCount(updated.getAutoResolvedCount());
        existing.setUnresolvedCount(updated.getUnresolvedCount());
        existing.setTotalGatewayAmount(updated.getTotalGatewayAmount());
        existing.setTotalSystemAmount(updated.getTotalSystemAmount());
        existing.setVarianceAmount(updated.getVarianceAmount());
        existing.setCompletedAt(updated.getCompletedAt());
        existing.setErrorMessage(updated.getErrorMessage());
        existing.tenant = updated.tenant;

        // Activity log
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }

    // --- Private helpers ---

    private void mapModelToEntity(ReconciliationBatch model, ReconciliationBatchEntity entity) {
        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setCreatedBy(model.getCreatedBy());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }
        entity.tenant = model.getTenant();

        // STM fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());

        // Business fields
        entity.setBatchDate(model.getBatchDate());
        entity.setPeriodStart(model.getPeriodStart());
        entity.setPeriodEnd(model.getPeriodEnd());
        entity.setGatewayType(model.getGatewayType());
        entity.setReconciliationMethod(model.getReconciliationMethod());
        entity.setMatchedCount(model.getMatchedCount());
        entity.setMismatchCount(model.getMismatchCount());
        entity.setAutoResolvedCount(model.getAutoResolvedCount());
        entity.setUnresolvedCount(model.getUnresolvedCount());
        entity.setTotalGatewayAmount(model.getTotalGatewayAmount());
        entity.setTotalSystemAmount(model.getTotalSystemAmount());
        entity.setVarianceAmount(model.getVarianceAmount());
        entity.setCompletedAt(model.getCompletedAt());
        entity.setErrorMessage(model.getErrorMessage());

        // Activity log
        if (model.obtainActivities() != null) {
            entity.setActivities(model.obtainActivities().stream()
                    .map(activityLog -> toActivityLogEntity((ReconciliationBatchActivityLog) activityLog))
                    .collect(Collectors.toList()));
        }
    }

    private ReconciliationBatchActivityLogEntity toActivityLogEntity(ReconciliationBatchActivityLog model) {
        if (model == null) return null;
        ReconciliationBatchActivityLogEntity entity = new ReconciliationBatchActivityLogEntity();
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private ReconciliationBatchActivityLog toActivityLogModel(ReconciliationBatchActivityLogEntity entity) {
        if (entity == null) return null;
        ReconciliationBatchActivityLog model = new ReconciliationBatchActivityLog();
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
