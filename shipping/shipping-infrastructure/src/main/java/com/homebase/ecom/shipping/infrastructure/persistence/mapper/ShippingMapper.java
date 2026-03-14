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
        model.setCarrier(entity.getCarrier());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setTrackingUrl(entity.getTrackingUrl());
        model.setShippedAt(entity.getShippedAt());
        model.setEstimatedDelivery(entity.getEstimatedDelivery());
        model.setShippingAddress(entity.getShippingAddress());
        model.setDeliveredAt(entity.getDeliveredAt());
        model.description = entity.getDescription();
        model.setDeliveryProof(entity.getDeliveryProof());
        model.setReturnReason(entity.getReturnReason());
        model.setReturnTrackingNumber(entity.getReturnTrackingNumber());
        model.setCurrentLocation(entity.getCurrentLocation());
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
        entity.setCarrier(model.getCarrier());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setTrackingUrl(model.getTrackingUrl());
        entity.setShippedAt(model.getShippedAt());
        entity.setEstimatedDelivery(model.getEstimatedDelivery());
        entity.setShippingAddress(model.getShippingAddress());
        entity.setDeliveredAt(model.getDeliveredAt());
        entity.setDescription(model.description);
        entity.setDeliveryProof(model.getDeliveryProof());
        entity.setReturnReason(model.getReturnReason());
        entity.setReturnTrackingNumber(model.getReturnTrackingNumber());
        entity.setCurrentLocation(model.getCurrentLocation());
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
