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
 * Maps ALL fields aligned with db-migrations/order/db.changelog-order.xml.
 */
public class OrderMapper {

    public Order toModel(OrderEntity entity) {
        if (entity == null) return null;
        Order model = new Order();

        // BaseEntity fields
        model.setId(entity.getId());
        model.setCreatedTime(entity.getCreatedTime());
        model.setLastModifiedTime(entity.getLastModifiedTime());
        model.setVersion(entity.getVersion());

        // STM fields
        model.setCurrentState(entity.getCurrentState());

        // Tenant
        model.setTenant(entity.tenant);

        // Core order fields
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
        model.setDescription(entity.getDescription());
        model.setItemCount(entity.getItemCount());
        model.setDiscountAmount(entity.getDiscountAmount());
        model.setShippingAddress(entity.getShippingAddress());

        // Cross-BC reference fields (order-004)
        model.setPaymentId(entity.getPaymentId());
        model.setCheckoutId(entity.getCheckoutId());
        model.setInvoiceNumber(entity.getInvoiceNumber());
        model.setInvoiceUrl(entity.getInvoiceUrl());
        model.setEstimatedDeliveryDate(entity.getEstimatedDeliveryDate());
        model.setActualDeliveryDate(entity.getActualDeliveryDate());
        model.setTrackingNumber(entity.getTrackingNumber());
        model.setCarrier(entity.getCarrier());
        model.setCouponCodes(entity.getCouponCodes());

        // SLA fields -- slaRedDate has getter in AbstractJpaStateEntity;
        // slaYellowDate is private in Chenile with no getter, so we skip it.
        model.setSlaRedDate(entity.getSlaRedDate());

        // Items
        if (entity.getItems() != null) {
            model.setItems(entity.getItems().stream()
                    .map(this::toItemModel)
                    .collect(Collectors.toList()));
        }

        // Activities
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

        // BaseEntity fields
        entity.setId(model.getId());
        entity.setCreatedTime(model.getCreatedTime());
        entity.setLastModifiedTime(model.getLastModifiedTime());
        if (model.getVersion() != null) {
            entity.setVersion(model.getVersion());
        }

        // STM fields
        entity.setCurrentState(model.getCurrentState());

        // Tenant
        entity.tenant = model.getTenant();

        // Core order fields
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
        entity.setDescription(model.getDescription());
        entity.setItemCount(model.getItemCount());
        entity.setDiscountAmount(model.getDiscountAmount());
        entity.setShippingAddress(model.getShippingAddress());

        // Cross-BC reference fields (order-004)
        entity.setPaymentId(model.getPaymentId());
        entity.setCheckoutId(model.getCheckoutId());
        entity.setInvoiceNumber(model.getInvoiceNumber());
        entity.setInvoiceUrl(model.getInvoiceUrl());
        entity.setEstimatedDeliveryDate(model.getEstimatedDeliveryDate());
        entity.setActualDeliveryDate(model.getActualDeliveryDate());
        entity.setTrackingNumber(model.getTrackingNumber());
        entity.setCarrier(model.getCarrier());
        entity.setCouponCodes(model.getCouponCodes());

        // SLA fields
        entity.setSlaRedDate(model.getSlaRedDate());

        // Items
        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                    .map(item -> {
                        OrderItemEntity itemEntity = toItemEntity(item);
                        itemEntity.setOrder(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList()));
        }

        // Activities
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

    /**
     * Merges incoming Order data onto an existing OrderEntity.
     * Preserves entity identity and JPA-managed fields; overwrites only non-null incoming values.
     * Null-safe for all nullable columns.
     */
    public OrderEntity mergeEntity(Order incoming, OrderEntity existing) {
        if (incoming == null) return existing;
        if (existing == null) return toEntity(incoming);

        // Core order fields -- only overwrite if incoming provides a value
        if (incoming.getOrderNumber() != null) existing.setOrderNumber(incoming.getOrderNumber());
        if (incoming.getCustomerId() != null) existing.setCustomerId(incoming.getCustomerId());
        if (incoming.getSubtotal() != null) existing.setSubtotal(incoming.getSubtotal());
        if (incoming.getTaxAmount() != null) existing.setTaxAmount(incoming.getTaxAmount());
        if (incoming.getShippingAmount() != null) existing.setShippingAmount(incoming.getShippingAmount());
        if (incoming.getTotalAmount() != null) existing.setTotalAmount(incoming.getTotalAmount());
        if (incoming.getCurrency() != null) existing.setCurrency(incoming.getCurrency());
        if (incoming.getShippingAddressId() != null) existing.setShippingAddressId(incoming.getShippingAddressId());
        if (incoming.getBillingAddressId() != null) existing.setBillingAddressId(incoming.getBillingAddressId());
        if (incoming.getPaymentMethodId() != null) existing.setPaymentMethodId(incoming.getPaymentMethodId());
        if (incoming.getNotes() != null) existing.setNotes(incoming.getNotes());
        if (incoming.getCancelReason() != null) existing.setCancelReason(incoming.getCancelReason());
        if (incoming.getDescription() != null) existing.setDescription(incoming.getDescription());
        existing.setItemCount(incoming.getItemCount());
        if (incoming.getDiscountAmount() != null) existing.setDiscountAmount(incoming.getDiscountAmount());
        if (incoming.getShippingAddress() != null) existing.setShippingAddress(incoming.getShippingAddress());

        // Cross-BC reference fields
        if (incoming.getPaymentId() != null) existing.setPaymentId(incoming.getPaymentId());
        if (incoming.getCheckoutId() != null) existing.setCheckoutId(incoming.getCheckoutId());
        if (incoming.getInvoiceNumber() != null) existing.setInvoiceNumber(incoming.getInvoiceNumber());
        if (incoming.getInvoiceUrl() != null) existing.setInvoiceUrl(incoming.getInvoiceUrl());
        if (incoming.getEstimatedDeliveryDate() != null) existing.setEstimatedDeliveryDate(incoming.getEstimatedDeliveryDate());
        if (incoming.getActualDeliveryDate() != null) existing.setActualDeliveryDate(incoming.getActualDeliveryDate());
        if (incoming.getTrackingNumber() != null) existing.setTrackingNumber(incoming.getTrackingNumber());
        if (incoming.getCarrier() != null) existing.setCarrier(incoming.getCarrier());
        if (incoming.getCouponCodes() != null) existing.setCouponCodes(incoming.getCouponCodes());

        // SLA fields
        if (incoming.getSlaRedDate() != null) existing.setSlaRedDate(incoming.getSlaRedDate());

        // STM state
        if (incoming.getCurrentState() != null) existing.setCurrentState(incoming.getCurrentState());

        // Tenant
        if (incoming.getTenant() != null) existing.tenant = incoming.getTenant();

        return existing;
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
        model.setSupplierId(entity.getSupplierId());
        model.setTaxAmount(entity.getTaxAmount());
        model.setDiscountAmount(entity.getDiscountAmount());
        model.setSupplierName(entity.getSupplierName());
        model.setImageUrl(entity.getImageUrl());
        model.setHsnCode(entity.getHsnCode());
        model.setReturnEligible(entity.getReturnEligible());
        model.setFulfillmentStatus(entity.getFulfillmentStatus());
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
        // Auto-compute totalPrice if not explicitly set
        if (model.getTotalPrice() != null) {
            entity.setTotalPrice(model.getTotalPrice());
        } else if (model.getUnitPrice() != null && model.getQuantity() != null) {
            entity.setTotalPrice(model.getUnitPrice().multiply(java.math.BigDecimal.valueOf(model.getQuantity())));
        } else {
            entity.setTotalPrice(model.getUnitPrice());
        }
        entity.setSupplierId(model.getSupplierId());
        entity.setTaxAmount(model.getTaxAmount());
        entity.setDiscountAmount(model.getDiscountAmount());
        entity.setSupplierName(model.getSupplierName());
        entity.setImageUrl(model.getImageUrl());
        entity.setHsnCode(model.getHsnCode());
        entity.setReturnEligible(model.getReturnEligible());
        entity.setFulfillmentStatus(model.getFulfillmentStatus());
        return entity;
    }

    /**
     * Merges fields from an updated OrderEntity onto an existing (JPA-managed) OrderEntity.
     * Used by ChenileJpaEntityStore to preserve @Version and JPA-managed state while
     * applying changes from STM transitions.
     */
    public void mergeJpaEntity(OrderEntity existing, OrderEntity updated) {
        // STM state
        if (updated.getCurrentState() != null) existing.setCurrentState(updated.getCurrentState());

        // Core order fields
        if (updated.getOrderNumber() != null) existing.setOrderNumber(updated.getOrderNumber());
        if (updated.getCustomerId() != null) existing.setCustomerId(updated.getCustomerId());
        if (updated.getSubtotal() != null) existing.setSubtotal(updated.getSubtotal());
        if (updated.getTaxAmount() != null) existing.setTaxAmount(updated.getTaxAmount());
        if (updated.getShippingAmount() != null) existing.setShippingAmount(updated.getShippingAmount());
        if (updated.getTotalAmount() != null) existing.setTotalAmount(updated.getTotalAmount());
        if (updated.getCurrency() != null) existing.setCurrency(updated.getCurrency());
        if (updated.getShippingAddressId() != null) existing.setShippingAddressId(updated.getShippingAddressId());
        if (updated.getBillingAddressId() != null) existing.setBillingAddressId(updated.getBillingAddressId());
        if (updated.getPaymentMethodId() != null) existing.setPaymentMethodId(updated.getPaymentMethodId());
        if (updated.getNotes() != null) existing.setNotes(updated.getNotes());
        if (updated.getCancelReason() != null) existing.setCancelReason(updated.getCancelReason());
        if (updated.getDescription() != null) existing.setDescription(updated.getDescription());
        existing.setItemCount(updated.getItemCount());
        if (updated.getDiscountAmount() != null) existing.setDiscountAmount(updated.getDiscountAmount());
        if (updated.getShippingAddress() != null) existing.setShippingAddress(updated.getShippingAddress());

        // Cross-BC reference fields
        if (updated.getPaymentId() != null) existing.setPaymentId(updated.getPaymentId());
        if (updated.getCheckoutId() != null) existing.setCheckoutId(updated.getCheckoutId());
        if (updated.getInvoiceNumber() != null) existing.setInvoiceNumber(updated.getInvoiceNumber());
        if (updated.getInvoiceUrl() != null) existing.setInvoiceUrl(updated.getInvoiceUrl());
        if (updated.getEstimatedDeliveryDate() != null) existing.setEstimatedDeliveryDate(updated.getEstimatedDeliveryDate());
        if (updated.getActualDeliveryDate() != null) existing.setActualDeliveryDate(updated.getActualDeliveryDate());
        if (updated.getTrackingNumber() != null) existing.setTrackingNumber(updated.getTrackingNumber());
        if (updated.getCarrier() != null) existing.setCarrier(updated.getCarrier());
        if (updated.getCouponCodes() != null) existing.setCouponCodes(updated.getCouponCodes());

        // SLA fields
        if (updated.getSlaRedDate() != null) existing.setSlaRedDate(updated.getSlaRedDate());

        // Tenant
        if (updated.tenant != null) existing.tenant = updated.tenant;

        // Activities -- clear and re-add to preserve Hibernate collection identity
        // (orphanRemoval=true requires same collection reference)
        if (updated.getActivities() != null) {
            existing.getActivities().clear();
            existing.getActivities().addAll(updated.getActivities());
        }
    }
}
