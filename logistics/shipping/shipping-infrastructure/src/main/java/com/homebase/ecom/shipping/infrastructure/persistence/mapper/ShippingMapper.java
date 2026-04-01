package com.homebase.ecom.shipping.infrastructure.persistence.mapper;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.model.ShippingActivityLog;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingActivityLogEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Maps between Shipping domain model and ShippingEntity JPA entity.
 * Covers all columns from DB migrations shipping-001 and shipping-003.
 * Wired as @Bean in ShippingConfiguration -- no @Component annotation.
 */
public class ShippingMapper {

    public Shipping toModel(ShippingEntity entity) {
        if (entity == null) return null;
        Shipping model = new Shipping();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        // STM fields
        model.setCurrentState(entity.getCurrentState());
        model.setStateEntryTime(entity.getStateEntryTime());

        // Tenant
        model.setTenant(entity.tenant);

        // Core identifiers
        model.setOrderId(entity.getOrderId());
        model.setCustomerId(entity.getCustomerId());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setCarrier(entity.getCarrier());
        model.setShippingMethod(entity.getShippingMethod());

        // Addresses
        model.setFromAddress(entity.getFromAddress());
        model.setToAddress(entity.getToAddress());

        // Package details (legacy)
        model.setWeight(entity.getWeight());
        model.setDimensions(entity.getDimensions());

        // Delivery tracking
        model.setEstimatedDeliveryDate(entity.getEstimatedDeliveryDate());
        model.setActualDeliveryDate(entity.getActualDeliveryDate());
        model.setDeliveryAttempts(entity.getDeliveryAttempts());
        model.setDeliveryInstructions(entity.getDeliveryInstructions());
        model.setCurrentLocation(entity.getCurrentLocation());

        // Description
        model.setDescription(entity.getDescription());

        // Migration-003 fields
        model.setShippingCost(entity.getShippingCost());
        model.setLabelUrl(entity.getLabelUrl());
        model.setReturnLabelUrl(entity.getReturnLabelUrl());
        model.setPodImageUrl(entity.getPodImageUrl());
        model.setCarrierTrackingUrl(entity.getCarrierTrackingUrl());
        model.setPackageWeightGrams(entity.getPackageWeightGrams());
        model.setPackageDimensionsJson(entity.getPackageDimensionsJson());
        model.setInsuranceAmount(entity.getInsuranceAmount());
        model.setSignatureRequired(entity.isSignatureRequired());

        // Activities
        if (entity.getActivities() != null) {
            List<ActivityLog> activityLogs = new ArrayList<>();
            for (ShippingActivityLogEntity actEntity : entity.getActivities()) {
                if (actEntity != null) {
                    ShippingActivityLog log = new ShippingActivityLog();
                    log.activityName = actEntity.getActivityName();
                    log.activitySuccess = actEntity.isActivitySuccess();
                    log.activityComment = actEntity.getActivityComment();
                    activityLogs.add(log);
                }
            }
            model.setActivities(activityLogs);
        }

        return model;
    }

    public ShippingEntity toEntity(Shipping model) {
        if (model == null) return null;
        ShippingEntity entity = new ShippingEntity();

        // BaseEntity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }

        // STM fields
        entity.setCurrentState(model.getCurrentState());
        entity.setStateEntryTime(model.getStateEntryTime());

        // Tenant
        entity.tenant = model.getTenant();

        // Core identifiers
        entity.setOrderId(model.getOrderId());
        entity.setCustomerId(model.getCustomerId());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setCarrier(model.getCarrier());
        entity.setShippingMethod(model.getShippingMethod());

        // Addresses
        entity.setFromAddress(model.getFromAddress());
        entity.setToAddress(model.getToAddress());

        // Package details (legacy)
        entity.setWeight(model.getWeight());
        entity.setDimensions(model.getDimensions());

        // Delivery tracking
        entity.setEstimatedDeliveryDate(model.getEstimatedDeliveryDate());
        entity.setActualDeliveryDate(model.getActualDeliveryDate());
        entity.setDeliveryAttempts(model.getDeliveryAttempts());
        entity.setDeliveryInstructions(model.getDeliveryInstructions());
        entity.setCurrentLocation(model.getCurrentLocation());

        // Description
        entity.setDescription(model.getDescription());

        // Migration-003 fields
        entity.setShippingCost(model.getShippingCost());
        entity.setLabelUrl(model.getLabelUrl());
        entity.setReturnLabelUrl(model.getReturnLabelUrl());
        entity.setPodImageUrl(model.getPodImageUrl());
        entity.setCarrierTrackingUrl(model.getCarrierTrackingUrl());
        entity.setPackageWeightGrams(model.getPackageWeightGrams());
        entity.setPackageDimensionsJson(model.getPackageDimensionsJson());
        entity.setInsuranceAmount(model.getInsuranceAmount());
        entity.setSignatureRequired(model.isSignatureRequired());

        // Activities
        if (model.obtainActivities() != null) {
            entity.setActivities(model.obtainActivities().stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList()));
        }

        return entity;
    }

    /**
     * Merges fields from an updated entity into an existing (DB-loaded) entity.
     * Preserves @Version and ID fields on both parent and child entities.
     */
    public void mergeEntity(ShippingEntity existing, ShippingEntity updated) {
        // Core identifiers
        existing.setOrderId(updated.getOrderId());
        existing.setCustomerId(updated.getCustomerId());
        existing.setTrackingNumber(updated.getTrackingNumber());
        existing.setCarrier(updated.getCarrier());
        existing.setShippingMethod(updated.getShippingMethod());

        // Addresses
        existing.setFromAddress(updated.getFromAddress());
        existing.setToAddress(updated.getToAddress());

        // Package details
        existing.setWeight(updated.getWeight());
        existing.setDimensions(updated.getDimensions());

        // Delivery tracking
        existing.setEstimatedDeliveryDate(updated.getEstimatedDeliveryDate());
        existing.setActualDeliveryDate(updated.getActualDeliveryDate());
        existing.setDeliveryAttempts(updated.getDeliveryAttempts());
        existing.setDeliveryInstructions(updated.getDeliveryInstructions());
        existing.setCurrentLocation(updated.getCurrentLocation());

        // Description
        existing.setDescription(updated.getDescription());

        // STM state
        existing.setCurrentState(updated.getCurrentState());

        // Migration-003 fields
        existing.setShippingCost(updated.getShippingCost());
        existing.setLabelUrl(updated.getLabelUrl());
        existing.setReturnLabelUrl(updated.getReturnLabelUrl());
        existing.setPodImageUrl(updated.getPodImageUrl());
        existing.setCarrierTrackingUrl(updated.getCarrierTrackingUrl());
        existing.setPackageWeightGrams(updated.getPackageWeightGrams());
        existing.setPackageDimensionsJson(updated.getPackageDimensionsJson());
        existing.setInsuranceAmount(updated.getInsuranceAmount());
        existing.setSignatureRequired(updated.isSignatureRequired());

        // Merge activities: match by ID, preserve versions
        Map<String, ShippingActivityLogEntity> existingActivitiesById = existing.getActivities().stream()
                .filter(a -> a.getId() != null)
                .collect(Collectors.toMap(ShippingActivityLogEntity::getId, Function.identity()));

        List<ShippingActivityLogEntity> mergedActivities = new ArrayList<>();
        if (updated.getActivities() != null) {
            for (ShippingActivityLogEntity updatedAct : updated.getActivities()) {
                ShippingActivityLogEntity existingAct = updatedAct.getId() != null
                        ? existingActivitiesById.get(updatedAct.getId()) : null;
                if (existingAct != null) {
                    existingAct.setActivityName(updatedAct.getActivityName());
                    existingAct.setActivitySuccess(updatedAct.isActivitySuccess());
                    existingAct.setActivityComment(updatedAct.getActivityComment());
                    mergedActivities.add(existingAct);
                } else {
                    mergedActivities.add(updatedAct);
                }
            }
        }
        existing.getActivities().clear();
        existing.getActivities().addAll(mergedActivities);
    }

    private ShippingActivityLogEntity toActivityEntity(ActivityLog model) {
        if (model == null) return null;
        ShippingActivityLogEntity entity = new ShippingActivityLogEntity();
        entity.setActivityName(model.getName());
        entity.setActivitySuccess(model.getSuccess());
        entity.setActivityComment(model.getComment());
        return entity;
    }
}
