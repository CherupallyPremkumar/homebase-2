package com.homebase.ecom.supplierlifecycle.infrastructure.persistence.mapper;

import java.util.ArrayList;
import java.util.List;

import org.chenile.stm.State;
import org.chenile.workflow.activities.model.ActivityLog;

import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSaga;
import com.homebase.ecom.supplierlifecycle.domain.model.SupplierLifecycleSagaActivityLog;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaActivityLogEntity;
import com.homebase.ecom.supplierlifecycle.infrastructure.persistence.entity.SupplierLifecycleSagaEntity;

/**
 * Maps between SupplierLifecycleSaga (domain) and SupplierLifecycleSagaEntity (JPA).
 * All fields from DB schema are mapped. Null-safe.
 */
public class SupplierLifecycleSagaMapper {

    public SupplierLifecycleSaga toModel(SupplierLifecycleSagaEntity entity) {
        if (entity == null) return null;
        SupplierLifecycleSaga model = new SupplierLifecycleSaga();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
        model.setVersion(entity.getVersion());
        model.setTenant(entity.tenant);

        // AbstractExtendedStateEntity fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());
        model.setSlaTendingLate(entity.getSlaTendingLate());
        model.setSlaLate(entity.getSlaLate());

        // Business fields
        model.setSupplierId(entity.getSupplierId());
        model.setAction(entity.getAction());
        model.setReason(entity.getReason());
        model.setProductsAffected(entity.getProductsAffected());
        model.setCatalogEntriesRemoved(entity.getCatalogEntriesRemoved());
        model.setInventoryFrozen(entity.getInventoryFrozen());
        model.setOrdersCancelled(entity.getOrdersCancelled());
        model.setErrorMessage(entity.getErrorMessage());
        model.setRetryCount(entity.getRetryCount());

        // flowId: extract from State if present
        State state = entity.getCurrentState();
        if (state != null && state.getFlowId() != null) {
            model.setFlowId(state.getFlowId());
        }

        // Activity logs
        if (entity.getActivities() != null) {
            List<ActivityLog> activities = new ArrayList<>();
            for (SupplierLifecycleSagaActivityLogEntity actEntity : entity.getActivities()) {
                SupplierLifecycleSagaActivityLog actModel = new SupplierLifecycleSagaActivityLog();
                actModel.activityName = actEntity.getName();
                actModel.activityComment = actEntity.getComment();
                actModel.activitySuccess = actEntity.getSuccess();
                activities.add(actModel);
            }
            model.setActivities(activities);
        }

        return model;
    }

    public SupplierLifecycleSagaEntity toEntity(SupplierLifecycleSaga model) {
        if (model == null) return null;
        SupplierLifecycleSagaEntity entity = new SupplierLifecycleSagaEntity();

        // BaseJpaEntity fields
        entity.setId(model.getId());
        entity.createdTime = model.getCreatedTime();
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.createdBy = model.getCreatedBy();
        if (model.getVersion() != null) {
            entity.version = model.getVersion();
        }
        entity.tenant = model.getTenant();

        // AbstractJpaStateEntity fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());
        entity.setSlaTendingLate(model.getSlaTendingLate());
        entity.setSlaLate(model.getSlaLate());

        // Business fields
        entity.setSupplierId(model.getSupplierId());
        entity.setAction(model.getAction());
        entity.setReason(model.getReason());
        entity.setProductsAffected(model.getProductsAffected());
        entity.setCatalogEntriesRemoved(model.getCatalogEntriesRemoved());
        entity.setInventoryFrozen(model.getInventoryFrozen());
        entity.setOrdersCancelled(model.getOrdersCancelled());
        entity.setErrorMessage(model.getErrorMessage());
        entity.setRetryCount(model.getRetryCount());

        // Activity logs
        if (model.getActivities() != null) {
            List<SupplierLifecycleSagaActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog actModel : model.getActivities()) {
                SupplierLifecycleSagaActivityLogEntity actEntity = new SupplierLifecycleSagaActivityLogEntity();
                actEntity.setActivityName(actModel.getName());
                actEntity.setActivityComment(actModel.getComment());
                actEntity.setActivitySuccess(actModel.getSuccess());
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }

    /**
     * Merges incoming domain model fields onto the existing JPA entity.
     * Used during STM processById: the STM retrieves the existing entity,
     * the transition action modifies the domain model, and then we merge
     * the changes back onto the existing entity for JPA persistence.
     */
    public SupplierLifecycleSagaEntity mergeEntity(SupplierLifecycleSaga model,
                                                     SupplierLifecycleSagaEntity existing) {
        if (model == null) return existing;
        if (existing == null) return toEntity(model);

        // State fields (always updated by STM)
        existing.setCurrentState(model.getCurrentState());
        existing.setStateEntryTime(model.getStateEntryTime());
        existing.setSlaTendingLate(model.getSlaTendingLate());
        existing.setSlaLate(model.getSlaLate());

        // Business fields that may change during transitions
        if (model.getSupplierId() != null) existing.setSupplierId(model.getSupplierId());
        if (model.getAction() != null) existing.setAction(model.getAction());
        if (model.getReason() != null) existing.setReason(model.getReason());
        existing.setProductsAffected(model.getProductsAffected());
        existing.setCatalogEntriesRemoved(model.getCatalogEntriesRemoved());
        existing.setInventoryFrozen(model.getInventoryFrozen());
        existing.setOrdersCancelled(model.getOrdersCancelled());
        existing.setErrorMessage(model.getErrorMessage());
        existing.setRetryCount(model.getRetryCount());

        // Activity logs -- rebuild from domain
        if (model.getActivities() != null) {
            List<SupplierLifecycleSagaActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog actModel : model.getActivities()) {
                SupplierLifecycleSagaActivityLogEntity actEntity = new SupplierLifecycleSagaActivityLogEntity();
                actEntity.setActivityName(actModel.getName());
                actEntity.setActivityComment(actModel.getComment());
                actEntity.setActivitySuccess(actModel.getSuccess());
                actEntities.add(actEntity);
            }
            existing.getActivities().clear();
            existing.getActivities().addAll(actEntities);
        }

        return existing;
    }
}
