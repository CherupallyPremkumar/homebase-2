package com.homebase.ecom.shipping.infrastructure.persistence.mapper;

import com.homebase.ecom.shipping.model.Shipping;
import com.homebase.ecom.shipping.model.ShippingActivityLog;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingEntity;
import com.homebase.ecom.shipping.infrastructure.persistence.entity.ShippingActivityLogEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShippingMapper {

    public Shipping toModel(ShippingEntity entity) {
        if (entity == null) return null;
        Shipping model = new Shipping();
        model.setId(entity.getId());
        model.setOrderId(entity.getOrderId());
        model.setCustomerId(entity.getCustomerId());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setCarrier(entity.getCarrier());
        model.setShippingMethod(entity.getShippingMethod());
        model.setFromAddress(entity.getFromAddress());
        model.setToAddress(entity.getToAddress());
        model.setWeight(entity.getWeight());
        model.setDimensions(entity.getDimensions());
        model.setEstimatedDeliveryDate(entity.getEstimatedDeliveryDate());
        model.setActualDeliveryDate(entity.getActualDeliveryDate());
        model.setDeliveryAttempts(entity.getDeliveryAttempts());
        model.setDeliveryInstructions(entity.getDeliveryInstructions());
        model.setCurrentLocation(entity.getCurrentLocation());
        model.description = entity.getDescription();
        model.setCurrentState(entity.getCurrentState());

        if (entity.getActivities() != null) {
            for (ShippingActivityLogEntity actEntity : entity.getActivities()) {
                if (actEntity != null) {
                    model.addActivity(actEntity.activityName, actEntity.activityComment);
                }
            }
        }
        return model;
    }

    public ShippingEntity toEntity(Shipping model) {
        if (model == null) return null;
        ShippingEntity entity = new ShippingEntity();
        entity.setId(model.getId());
        entity.setOrderId(model.getOrderId());
        entity.setCustomerId(model.getCustomerId());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setCarrier(model.getCarrier());
        entity.setShippingMethod(model.getShippingMethod());
        entity.setFromAddress(model.getFromAddress());
        entity.setToAddress(model.getToAddress());
        entity.setWeight(model.getWeight());
        entity.setDimensions(model.getDimensions());
        entity.setEstimatedDeliveryDate(model.getEstimatedDeliveryDate());
        entity.setActualDeliveryDate(model.getActualDeliveryDate());
        entity.setDeliveryAttempts(model.getDeliveryAttempts());
        entity.setDeliveryInstructions(model.getDeliveryInstructions());
        entity.setCurrentLocation(model.getCurrentLocation());
        entity.setDescription(model.description);
        entity.setCurrentState(model.getCurrentState());

        if (model.obtainActivities() != null) {
            entity.activities = model.obtainActivities().stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList());
        }
        return entity;
    }

    private ShippingActivityLogEntity toActivityEntity(org.chenile.workflow.activities.model.ActivityLog model) {
        if (model == null) return null;
        ShippingActivityLogEntity entity = new ShippingActivityLogEntity();
        entity.activityName = model.getName();
        entity.activitySuccess = model.getSuccess();
        entity.activityComment = model.getComment();
        return entity;
    }
}
