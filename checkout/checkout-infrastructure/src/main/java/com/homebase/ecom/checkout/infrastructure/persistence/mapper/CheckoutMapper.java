package com.homebase.ecom.checkout.infrastructure.persistence.mapper;

import com.homebase.ecom.checkout.model.Checkout;
import com.homebase.ecom.checkout.model.CheckoutItem;
import com.homebase.ecom.checkout.infrastructure.persistence.entity.CheckoutEntity;
import com.homebase.ecom.checkout.infrastructure.persistence.entity.CheckoutItemEntity;
import com.homebase.ecom.shared.Money;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CheckoutMapper {

    public CheckoutEntity toEntity(Checkout model) {
        if (model == null) return null;
        CheckoutEntity entity = new CheckoutEntity();
        entity.setId(model.getId());
        entity.setCustomerId(model.getCustomerId());
        entity.setCartId(model.getCartId());
        entity.setOrderId(model.getOrderId());
        entity.setPaymentId(model.getPaymentId());
        entity.setSubtotal(model.getSubtotal().getAmount());
        entity.setCurrency(model.getSubtotal().getCurrency());
        entity.setDiscountAmount(model.getDiscountAmount().getAmount());
        entity.setShippingCost(model.getShippingCost().getAmount());
        entity.setTaxAmount(model.getTaxAmount().getAmount());
        entity.setTotal(model.getTotal().getAmount());
        entity.setShippingAddressId(model.getShippingAddressId());
        entity.setBillingAddressId(model.getBillingAddressId());
        entity.setShippingMethod(model.getShippingMethod());
        entity.setPaymentMethodId(model.getPaymentMethodId());
        entity.setLastCompletedStep(model.getLastCompletedStep());
        entity.setFailureReason(model.getFailureReason());
        entity.setExpiresAt(model.getExpiresAt());
        entity.description = model.description;
        entity.setCurrentState(model.getCurrentState());
        entity.tenant = model.getTenant();

        // Fields from checkout-003 migration
        entity.setIdempotencyKey(model.getIdempotencyKey());
        entity.setPriceLockToken(model.getPriceLockToken());
        entity.setPaymentSessionId(model.getPaymentSessionId());
        entity.setCompletedAt(model.getCompletedAt());
        entity.setCancelledAt(model.getCancelledAt());

        if (model.getCouponCodes() != null && !model.getCouponCodes().isEmpty()) {
            entity.setCouponCodes(String.join(",", model.getCouponCodes()));
        }

        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList()));
        }

        return entity;
    }

    public Checkout toModel(CheckoutEntity entity) {
        if (entity == null) return null;
        Checkout model = new Checkout();
        model.setId(entity.getId());
        model.setCustomerId(entity.getCustomerId());
        model.setCartId(entity.getCartId());
        model.setOrderId(entity.getOrderId());
        model.setPaymentId(entity.getPaymentId());
        String currency = entity.getCurrency() != null ? entity.getCurrency() : "INR";
        model.setSubtotal(Money.of(entity.getSubtotal(), currency));
        model.setDiscountAmount(Money.of(entity.getDiscountAmount(), currency));
        model.setShippingCost(Money.of(entity.getShippingCost(), currency));
        model.setTaxAmount(Money.of(entity.getTaxAmount(), currency));
        model.setTotal(Money.of(entity.getTotal(), currency));
        model.setShippingAddressId(entity.getShippingAddressId());
        model.setBillingAddressId(entity.getBillingAddressId());
        model.setShippingMethod(entity.getShippingMethod());
        model.setPaymentMethodId(entity.getPaymentMethodId());
        model.setLastCompletedStep(entity.getLastCompletedStep());
        model.setFailureReason(entity.getFailureReason());
        model.setExpiresAt(entity.getExpiresAt());
        model.description = entity.description;
        model.setCurrentState(entity.getCurrentState());
        model.setTenant(entity.tenant);

        // Fields from checkout-003 migration
        model.setIdempotencyKey(entity.getIdempotencyKey());
        model.setPriceLockToken(entity.getPriceLockToken());
        model.setPaymentSessionId(entity.getPaymentSessionId());
        model.setCompletedAt(entity.getCompletedAt());
        model.setCancelledAt(entity.getCancelledAt());

        if (entity.getCouponCodes() != null && !entity.getCouponCodes().isEmpty()) {
            model.setCouponCodes(new ArrayList<>(Arrays.asList(entity.getCouponCodes().split(","))));
        } else {
            model.setCouponCodes(new ArrayList<>());
        }

        if (entity.getItems() != null) {
            model.setItems(entity.getItems().stream()
                .map(itemEntity -> toItemModel(itemEntity, currency))
                .collect(Collectors.toList()));
        }

        return model;
    }

    public void mergeEntity(CheckoutEntity existing, CheckoutEntity updated) {
        existing.setCustomerId(updated.getCustomerId());
        existing.setCartId(updated.getCartId());
        existing.setOrderId(updated.getOrderId());
        existing.setPaymentId(updated.getPaymentId());
        existing.setSubtotal(updated.getSubtotal());
        existing.setCurrency(updated.getCurrency());
        existing.setDiscountAmount(updated.getDiscountAmount());
        existing.setShippingCost(updated.getShippingCost());
        existing.setTaxAmount(updated.getTaxAmount());
        existing.setTotal(updated.getTotal());
        existing.setCouponCodes(updated.getCouponCodes());
        existing.setShippingAddressId(updated.getShippingAddressId());
        existing.setBillingAddressId(updated.getBillingAddressId());
        existing.setShippingMethod(updated.getShippingMethod());
        existing.setPaymentMethodId(updated.getPaymentMethodId());
        existing.setLastCompletedStep(updated.getLastCompletedStep());
        existing.setFailureReason(updated.getFailureReason());
        existing.setExpiresAt(updated.getExpiresAt());
        existing.description = updated.description;
        existing.setCurrentState(updated.getCurrentState());

        // Fields from checkout-003 migration
        existing.setIdempotencyKey(updated.getIdempotencyKey());
        existing.setPriceLockToken(updated.getPriceLockToken());
        existing.setPaymentSessionId(updated.getPaymentSessionId());
        existing.setCompletedAt(updated.getCompletedAt());
        existing.setCancelledAt(updated.getCancelledAt());

        // Merge items
        Map<String, CheckoutItemEntity> existingItemsById = existing.getItems().stream()
                .filter(i -> i.getId() != null)
                .collect(Collectors.toMap(CheckoutItemEntity::getId, Function.identity()));

        List<CheckoutItemEntity> mergedItems = new ArrayList<>();
        if (updated.getItems() != null) {
            for (CheckoutItemEntity updatedItem : updated.getItems()) {
                CheckoutItemEntity existingItem = updatedItem.getId() != null
                        ? existingItemsById.get(updatedItem.getId()) : null;
                if (existingItem != null) {
                    existingItem.setProductId(updatedItem.getProductId());
                    existingItem.setVariantId(updatedItem.getVariantId());
                    existingItem.setSku(updatedItem.getSku());
                    existingItem.setProductName(updatedItem.getProductName());
                    existingItem.setSupplierId(updatedItem.getSupplierId());
                    existingItem.setQuantity(updatedItem.getQuantity());
                    existingItem.setUnitPrice(updatedItem.getUnitPrice());
                    existingItem.setLineTotal(updatedItem.getLineTotal());
                    existingItem.setCheckout(existing);
                    mergedItems.add(existingItem);
                } else {
                    updatedItem.setCheckout(existing);
                    mergedItems.add(updatedItem);
                }
            }
        }
        existing.getItems().clear();
        existing.getItems().addAll(mergedItems);
    }

    private CheckoutItemEntity toItemEntity(CheckoutItem model, CheckoutEntity checkoutEntity) {
        if (model == null) return null;
        CheckoutItemEntity entity = new CheckoutItemEntity();
        entity.setCheckout(checkoutEntity);
        entity.setProductId(model.getProductId());
        entity.setVariantId(model.getVariantId());
        entity.setSku(model.getSku());
        entity.setProductName(model.getProductName());
        entity.setSupplierId(model.getSupplierId());
        entity.setQuantity(model.getQuantity());
        entity.setUnitPrice(model.getUnitPrice() != null ? model.getUnitPrice().getAmount() : 0);
        entity.setLineTotal(model.getLineTotal() != null ? model.getLineTotal().getAmount() : 0);
        return entity;
    }

    private CheckoutItem toItemModel(CheckoutItemEntity entity, String currency) {
        if (entity == null) return null;
        CheckoutItem model = new CheckoutItem();
        model.setProductId(entity.getProductId());
        model.setVariantId(entity.getVariantId());
        model.setSku(entity.getSku());
        model.setProductName(entity.getProductName());
        model.setSupplierId(entity.getSupplierId());
        model.setQuantity(entity.getQuantity());
        model.setUnitPrice(Money.of(entity.getUnitPrice(), currency));
        model.setLineTotal(Money.of(entity.getLineTotal(), currency));
        return model;
    }
}
