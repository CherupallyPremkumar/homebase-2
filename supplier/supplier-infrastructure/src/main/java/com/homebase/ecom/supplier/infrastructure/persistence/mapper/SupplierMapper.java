package com.homebase.ecom.supplier.infrastructure.persistence.mapper;

import com.homebase.ecom.supplier.model.Supplier;
import com.homebase.ecom.supplier.model.SupplierActivityLog;
import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierActivityLogEntity;
import com.homebase.ecom.supplier.infrastructure.persistence.entity.SupplierEntity;

import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Domain and JPA entities.
 * No Spring annotations -- wired in configuration.
 */
public class SupplierMapper {

    public Supplier toModel(SupplierEntity entity) {
        if (entity == null) return null;

        Supplier model = new Supplier();
        // Base entity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        // STM state
        model.setCurrentState(entity.getCurrentState());

        // Supplier fields
        model.setName(entity.getName());
        model.setUserId(entity.getUserId());
        model.setEmail(entity.getEmail());
        model.setDescription(entity.getDescription());
        model.setPhone(entity.getPhone());
        model.setUpiId(entity.getUpiId());
        model.setAddress(entity.getAddress());
        model.setCommissionPercentage(entity.getCommissionPercentage());

        // Lifecycle tracking fields
        model.setActiveDate(entity.getActiveDate());
        model.setRejectionReason(entity.getRejectionReason());
        model.setSuspensionReason(entity.getSuspensionReason());
        model.setBlacklistReason(entity.getBlacklistReason());
        model.setSuspendedDate(entity.getSuspendedDate());
        model.setBlacklistedDate(entity.getBlacklistedDate());
        model.setProductsDisabled(entity.isProductsDisabled());

        if (entity.getActivities() != null) {
            for (SupplierActivityLogEntity logEntity : entity.getActivities()) {
                model.addActivity(logEntity.getName(), logEntity.getComment());
            }
        }

        return model;
    }

    public SupplierEntity toEntity(Supplier model) {
        if (model == null) return null;

        SupplierEntity entity = new SupplierEntity();
        // Base entity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        entity.setVersion(model.getVersion());

        // STM state
        entity.setCurrentState(model.getCurrentState());

        // Supplier fields
        entity.setName(model.getName());
        entity.setUserId(model.getUserId());
        entity.setEmail(model.getEmail());
        entity.setDescription(model.getDescription());
        entity.setPhone(model.getPhone());
        entity.setUpiId(model.getUpiId());
        entity.setAddress(model.getAddress());
        entity.setCommissionPercentage(model.getCommissionPercentage());

        // Lifecycle tracking fields
        entity.setActiveDate(model.getActiveDate());
        entity.setRejectionReason(model.getRejectionReason());
        entity.setSuspensionReason(model.getSuspensionReason());
        entity.setBlacklistReason(model.getBlacklistReason());
        entity.setSuspendedDate(model.getSuspendedDate());
        entity.setBlacklistedDate(model.getBlacklistedDate());
        entity.setProductsDisabled(model.isProductsDisabled());

        if (model.obtainActivities() != null) {
            entity.setActivities(model.obtainActivities().stream()
                    .map(this::toEntity)
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    public SupplierActivityLogEntity toEntity(org.chenile.workflow.activities.model.ActivityLog model) {
        if (model == null) return null;
        SupplierActivityLogEntity entity = new SupplierActivityLogEntity();
        entity.setName(model.getName());
        entity.setComment(model.getComment());
        entity.setSuccess(model.getSuccess());
        return entity;
    }
}
