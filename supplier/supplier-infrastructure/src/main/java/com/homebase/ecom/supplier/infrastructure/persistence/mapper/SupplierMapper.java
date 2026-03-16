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
        model.setVersion(entity.getVersion() != null ? entity.getVersion() : 0L);

        // STM state
        model.setCurrentState(entity.getCurrentState());

        // Identity & Business Info
        model.setUserId(entity.getUserId());
        model.setBusinessName(entity.getBusinessName());
        model.setBusinessType(entity.getBusinessType());
        model.setTaxId(entity.getTaxId());
        model.setBankAccountId(entity.getBankAccountId());
        model.setContactEmail(entity.getContactEmail());
        model.setContactPhone(entity.getContactPhone());
        model.setAddress(entity.getAddress());

        // Performance Metrics
        model.setRating(entity.getRating());
        model.setTotalOrders(entity.getTotalOrders());
        model.setTotalReturns(entity.getTotalReturns());
        model.setFulfillmentRate(entity.getFulfillmentRate());
        model.setAvgShippingDays(entity.getAvgShippingDays());
        model.setCommissionRate(entity.getCommissionRate());

        // Lifecycle tracking
        model.setActiveDate(entity.getActiveDate());
        model.setRejectionReason(entity.getRejectionReason());
        model.setSuspensionReason(entity.getSuspensionReason());
        model.setTerminationReason(entity.getTerminationReason());
        model.setProbationReason(entity.getProbationReason());
        model.setSuspendedDate(entity.getSuspendedDate());
        model.setTerminatedDate(entity.getTerminatedDate());
        model.setProbationDate(entity.getProbationDate());
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
        entity.setVersion(model.getVersion() != null ? model.getVersion() : 0L);

        // STM state
        entity.setCurrentState(model.getCurrentState());

        // Identity & Business Info
        entity.setUserId(model.getUserId());
        entity.setBusinessName(model.getBusinessName());
        entity.setBusinessType(model.getBusinessType());
        entity.setTaxId(model.getTaxId());
        entity.setBankAccountId(model.getBankAccountId());
        entity.setContactEmail(model.getContactEmail());
        entity.setContactPhone(model.getContactPhone());
        entity.setAddress(model.getAddress());

        // Performance Metrics
        entity.setRating(model.getRating());
        entity.setTotalOrders(model.getTotalOrders());
        entity.setTotalReturns(model.getTotalReturns());
        entity.setFulfillmentRate(model.getFulfillmentRate());
        entity.setAvgShippingDays(model.getAvgShippingDays());
        entity.setCommissionRate(model.getCommissionRate());

        // Lifecycle tracking
        entity.setActiveDate(model.getActiveDate());
        entity.setRejectionReason(model.getRejectionReason());
        entity.setSuspensionReason(model.getSuspensionReason());
        entity.setTerminationReason(model.getTerminationReason());
        entity.setProbationReason(model.getProbationReason());
        entity.setSuspendedDate(model.getSuspendedDate());
        entity.setTerminatedDate(model.getTerminatedDate());
        entity.setProbationDate(model.getProbationDate());
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
