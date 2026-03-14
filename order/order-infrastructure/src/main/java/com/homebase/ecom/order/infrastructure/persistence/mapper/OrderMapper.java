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

public class OrderMapper {

    public Order toModel(OrderEntity entity) {
        if (entity == null) return null;
        Order model = new Order();
        model.setId(entity.getId());
        model.setUser_Id(entity.getUserId());
        model.setGatewaySessionId(entity.getGatewaySessionId());
        model.setGatewayTransactionId(entity.getGatewayTransactionId());
        model.setStatus(entity.getStatus());
        model.setTotalAmount(entity.getTotalAmount());
        model.setTaxAmount(entity.getTaxAmount());
        model.setShippingAmount(entity.getShippingAmount());
        model.setIdempotencyKey(entity.getIdempotencyKey());
        model.setMetadata(entity.getMetadata());
        model.setWebhookProcessedAt(entity.getWebhookProcessedAt());
        model.setShippingAddress(entity.getShippingAddress());
        model.setBillingAddress(entity.getBillingAddress());
        model.setAppliedPromoCode(entity.getAppliedPromoCode());
        model.setDiscountAmount(entity.getDiscountAmount());
        model.setCartId(entity.getCartId());
        model.setRetryCount(entity.getRetryCount());
        model.setPreviousFailedOrderId(entity.getPreviousFailedOrderId());
        model.setDeliveryDate(entity.getDeliveryDate());
        model.description = entity.getDescription();
        model.setCurrentState(entity.getCurrentState());

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
        entity.setUserId(model.getUser_Id());
        entity.setGatewaySessionId(model.getGatewaySessionId());
        entity.setGatewayTransactionId(model.getGatewayTransactionId());
        entity.setStatus(model.getStatus());
        entity.setTotalAmount(model.getTotalAmount());
        entity.setTaxAmount(model.getTaxAmount());
        entity.setShippingAmount(model.getShippingAmount());
        entity.setIdempotencyKey(model.getIdempotencyKey());
        entity.setMetadata(model.getMetadata());
        entity.setWebhookProcessedAt(model.getWebhookProcessedAt());
        entity.setShippingAddress(model.getShippingAddress());
        entity.setBillingAddress(model.getBillingAddress());
        entity.setAppliedPromoCode(model.getAppliedPromoCode());
        entity.setDiscountAmount(model.getDiscountAmount());
        entity.setCartId(model.getCartId());
        entity.setRetryCount(model.getRetryCount());
        entity.setPreviousFailedOrderId(model.getPreviousFailedOrderId());
        entity.setDeliveryDate(model.getDeliveryDate());
        entity.setDescription(model.description);
        entity.setCurrentState(model.getCurrentState());

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
        model.setSupplierId(entity.getSupplierId());
        model.setProductName(entity.getProductName());
        model.setQuantity(entity.getQuantity());
        model.setUnitPrice(entity.getUnitPrice());
        model.setTotalPrice(entity.getTotalPrice());
        model.setStatus(entity.getStatus());
        model.setSettlementStatus(entity.getSettlementStatus());
        // Set settlementId after settlementStatus to avoid auto-setting to SETTLED
        if (entity.getSettlementId() != null) {
            model.setSettlementId(entity.getSettlementId());
        }
        return model;
    }

    public OrderItemEntity toItemEntity(OrderItem model) {
        if (model == null) return null;
        OrderItemEntity entity = new OrderItemEntity();
        entity.setId(model.getId());
        entity.setProductId(model.getProductId());
        entity.setSupplierId(model.getSupplierId());
        entity.setProductName(model.getProductName());
        entity.setQuantity(model.getQuantity());
        entity.setUnitPrice(model.getUnitPrice());
        entity.setTotalPrice(model.getTotalPrice());
        entity.setStatus(model.getStatus());
        entity.setSettlementStatus(model.getSettlementStatus());
        if (model.getSettlementId() != null) {
            entity.setSettlementId(model.getSettlementId());
        }
        return entity;
    }
}
