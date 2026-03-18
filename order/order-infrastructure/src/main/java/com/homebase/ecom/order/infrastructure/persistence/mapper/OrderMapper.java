package com.homebase.ecom.order.infrastructure.persistence.mapper;

import com.homebase.ecom.order.model.Order;
import com.homebase.ecom.order.model.OrderActivityLog;
import com.homebase.ecom.order.model.OrderItem;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderActivityLogEntity;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderEntity;
import com.homebase.ecom.order.infrastructure.persistence.entity.OrderItemEntity;
import org.chenile.workflow.activities.model.ActivityLog;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Bidirectional mapper between Order domain model and OrderEntity JPA entity.
 */
public class OrderMapper {

    public Order toModel(OrderEntity entity) {
        if (entity == null) return null;
        Order model = new Order();
        model.setId(entity.getId());
        model.setOrderNumber(entity.getOrderNumber());
        model.setCustomerId(entity.getCustomerId());
        model.setSubtotal(entity.getSubtotal());
        model.setTaxAmount(entity.getTaxAmount());
        model.setShippingAmount(entity.getShippingAmount());
        model.setTotalAmount(entity.getTotalAmount());
        model.setCurrency(entity.getCurrency());
        model.setShippingAddressId(entity.getShippingAddressId());
        model.setBillingAddressId(entity.getBillingAddressId());
        model.setPaymentMethodId(entity.getPaymentMethodId());
        model.setNotes(entity.getNotes());
        model.setCancelReason(entity.getCancelReason());
        model.description = entity.getDescription();
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);

        if (entity.getItems() != null) {
            model.setItems(entity.getItems().stream()
                    .map(this::toItemModel)
                    .collect(Collectors.toList()));
        }

        if (entity.getActivities() != null) {
            for (OrderActivityLogEntity actEntity : entity.getActivities()) {
                model.addActivity(actEntity.getName(), actEntity.getComment());
            }
        }

        return model;
    }

    public OrderEntity toEntity(Order model) {
        if (model == null) return null;
        OrderEntity entity = new OrderEntity();
        entity.setId(model.getId());
        entity.setOrderNumber(model.getOrderNumber());
        entity.setCustomerId(model.getCustomerId());
        entity.setSubtotal(model.getSubtotal());
        entity.setTaxAmount(model.getTaxAmount());
        entity.setShippingAmount(model.getShippingAmount());
        entity.setTotalAmount(model.getTotalAmount());
        entity.setCurrency(model.getCurrency());
        entity.setShippingAddressId(model.getShippingAddressId());
        entity.setBillingAddressId(model.getBillingAddressId());
        entity.setPaymentMethodId(model.getPaymentMethodId());
        entity.setNotes(model.getNotes());
        entity.setCancelReason(model.getCancelReason());
        entity.setDescription(model.description);
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                    .map(item -> {
                        OrderItemEntity itemEntity = toItemEntity(item);
                        itemEntity.setOrder(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList()));
        }

        if (model.obtainActivities() != null) {
            ArrayList<OrderActivityLogEntity> actEntities = new ArrayList<>();
            for (ActivityLog act : model.obtainActivities()) {
                OrderActivityLogEntity actEntity = new OrderActivityLogEntity();
                actEntity.activityName = act.getName();
                actEntity.activitySuccess = act.getSuccess();
                actEntity.activityComment = act.getComment();
                actEntities.add(actEntity);
            }
            entity.setActivities(actEntities);
        }

        return entity;
    }

    public OrderItem toItemModel(OrderItemEntity entity) {
        if (entity == null) return null;
        OrderItem model = new OrderItem();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setVariantId(entity.getVariantId());
        model.setSku(entity.getSku());
        model.setProductName(entity.getProductName());
        model.setQuantity(entity.getQuantity());
        model.setUnitPrice(entity.getUnitPrice());
        model.setTotalPrice(entity.getTotalPrice());
        return model;
    }

    public OrderItemEntity toItemEntity(OrderItem model) {
        if (model == null) return null;
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(model.getId());
        entity.setProductId(model.getProductId());
        entity.setVariantId(model.getVariantId());
        entity.setSku(model.getSku());
        entity.setProductName(model.getProductName());
        entity.setQuantity(model.getQuantity());
        entity.setUnitPrice(model.getUnitPrice());
        entity.setTotalPrice(model.getTotalPrice());
        return entity;
    }
}
