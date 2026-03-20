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
        model.setLastModifiedBy(entity.getLastModifiedBy());
        model.setCreatedBy(entity.getCreatedBy());
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

        // Performance Metrics (null-safe defaults for new suppliers)
        model.setRating(entity.getRating() != null ? entity.getRating() : 0.0);
        model.setTotalOrders(entity.getTotalOrders() != null ? entity.getTotalOrders() : 0);
        model.setTotalReturns(entity.getTotalReturns() != null ? entity.getTotalReturns() : 0);
        model.setFulfillmentRate(entity.getFulfillmentRate() != null ? entity.getFulfillmentRate() : 0.0);
        model.setAvgShippingDays(entity.getAvgShippingDays());
        model.setCommissionRate(entity.getCommissionRate() != null ? entity.getCommissionRate() : 0.0);

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
        model.setDeletedAt(entity.getDeletedAt());
        model.setTenant(entity.tenant);

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
        entity.setLastModifiedBy(model.getLastModifiedBy());
        entity.setCreatedBy(model.getCreatedBy());
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
        entity.setDeletedAt(model.getDeletedAt());
        entity.tenant = model.getTenant();

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

    /**
     * Merges updated fields from a new entity into an existing managed JPA entity.
     * Used by ChenileJpaEntityStore to properly update managed entities
     * and preserve optimistic locking via @Version.
     */
    public void mergeEntity(SupplierEntity existing, SupplierEntity updated) {
        // STM state (critical)
        existing.setCurrentState(updated.getCurrentState());
        existing.setStateEntryTime(updated.getStateEntryTime());
        existing.setSlaTendingLate(updated.getSlaTendingLate());
        existing.setSlaLate(updated.getSlaLate());

        // Identity & Business Info
        existing.setUserId(updated.getUserId());
        existing.setBusinessName(updated.getBusinessName());
        existing.setBusinessType(updated.getBusinessType());
        existing.setTaxId(updated.getTaxId());
        existing.setBankAccountId(updated.getBankAccountId());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setAddress(updated.getAddress());

        // Performance Metrics
        existing.setRating(updated.getRating());
        existing.setTotalOrders(updated.getTotalOrders());
        existing.setTotalReturns(updated.getTotalReturns());
        existing.setFulfillmentRate(updated.getFulfillmentRate());
        existing.setAvgShippingDays(updated.getAvgShippingDays());
        existing.setCommissionRate(updated.getCommissionRate());

        // Lifecycle Tracking
        existing.setActiveDate(updated.getActiveDate());
        existing.setRejectionReason(updated.getRejectionReason());
        existing.setSuspensionReason(updated.getSuspensionReason());
        existing.setTerminationReason(updated.getTerminationReason());
        existing.setProbationReason(updated.getProbationReason());
        existing.setSuspendedDate(updated.getSuspendedDate());
        existing.setTerminatedDate(updated.getTerminatedDate());
        existing.setProbationDate(updated.getProbationDate());
        existing.setProductsDisabled(updated.isProductsDisabled());
        existing.setDeletedAt(updated.getDeletedAt());
        existing.tenant = updated.tenant;

        // Audit
        existing.setLastModifiedBy(updated.getLastModifiedBy());
        existing.setLastModifiedTime(updated.getLastModifiedTime());

        // Activities -- merge by clearing and re-adding
        existing.getActivities().clear();
        if (updated.getActivities() != null) {
            existing.getActivities().addAll(updated.getActivities());
        }
    }
}
