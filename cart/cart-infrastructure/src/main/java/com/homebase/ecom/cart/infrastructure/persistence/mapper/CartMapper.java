package com.homebase.ecom.cart.infrastructure.persistence.mapper;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartActivityLog;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartActivityLogEntity;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartItemEntity;
import com.homebase.ecom.shared.Money;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartEntity toEntity(Cart model) {
        if (model == null) return null;
        CartEntity entity = new CartEntity();
        entity.setId(model.getId());
        entity.setCustomerId(model.getCustomerId());
        entity.setSessionId(model.getSessionId());
        entity.setSubtotal(model.getSubtotal().getAmount());
        entity.setCurrency(model.getCurrency());
        entity.setDiscountAmount(model.getDiscountAmount().getAmount());
        entity.setTotal(model.getTotal().getAmount());
        entity.setExpiresAt(model.getExpiresAt());
        entity.setNotes(model.getNotes());
        entity.setOrderId(model.getOrderId());
        entity.setPriceChanged(model.isPriceChanged());
        entity.description = model.description;
        entity.setCurrentState(model.getCurrentState());

        // Coupon codes: List<String> -> comma-separated string
        if (model.getCouponCodes() != null && !model.getCouponCodes().isEmpty()) {
            entity.setCouponCodes(String.join(",", model.getCouponCodes()));
        }

        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList()));
        }

        if (model.activities != null) {
            entity.setActivities(model.activities.stream()
                .map(this::toActivityEntity)
                .collect(Collectors.toList()));
        }

        return entity;
    }

    public Cart toModel(CartEntity entity) {
        if (entity == null) return null;
        Cart model = new Cart();
        model.setId(entity.getId());
        model.setCustomerId(entity.getCustomerId());
        model.setSessionId(entity.getSessionId());
        String currency = entity.getCurrency() != null ? entity.getCurrency() : "INR";
        model.setSubtotal(Money.of(entity.getSubtotal(), currency));
        model.setDiscountAmount(Money.of(entity.getDiscountAmount(), currency));
        model.setTotal(Money.of(entity.getTotal(), currency));
        model.setExpiresAt(entity.getExpiresAt());
        model.setNotes(entity.getNotes());
        model.setOrderId(entity.getOrderId());
        model.setPriceChanged(entity.isPriceChanged());
        model.description = entity.description;
        model.setCurrentState(entity.getCurrentState());

        // Coupon codes: comma-separated string -> List<String>
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

        if (entity.getActivities() != null) {
            model.activities = entity.getActivities().stream()
                .map(this::toActivityModel)
                .collect(Collectors.toList());
        }

        return model;
    }

    private CartItemEntity toItemEntity(CartItem model, CartEntity cartEntity) {
        if (model == null) return null;
        CartItemEntity entity = new CartItemEntity();
        entity.setId(model.getId());
        entity.setCart(cartEntity);
        entity.setProductId(model.getProductId());
        entity.setVariantId(model.getVariantId());
        entity.setSku(model.getSku());
        entity.setProductName(model.getProductName());
        entity.setSupplierId(model.getSupplierId());
        entity.setQuantity(model.getQuantity());
        entity.setUnitPrice(model.getUnitPrice().getAmount());
        entity.setLineTotal(model.getLineTotal().getAmount());
        entity.setAvailable(model.isAvailable());
        return entity;
    }

    /**
     * Merges fields from updated entity into existing (DB-loaded) entity.
     * Preserves @Version fields on both parent and child entities.
     */
    public void mergeEntity(CartEntity existing, CartEntity updated) {
        existing.setCustomerId(updated.getCustomerId());
        existing.setSessionId(updated.getSessionId());
        existing.setSubtotal(updated.getSubtotal());
        existing.setCurrency(updated.getCurrency());
        existing.setDiscountAmount(updated.getDiscountAmount());
        existing.setTotal(updated.getTotal());
        existing.setCouponCodes(updated.getCouponCodes());
        existing.setExpiresAt(updated.getExpiresAt());
        existing.setNotes(updated.getNotes());
        existing.setOrderId(updated.getOrderId());
        existing.setPriceChanged(updated.isPriceChanged());
        existing.description = updated.description;
        existing.setCurrentState(updated.getCurrentState());

        // Merge items: match by ID, update existing, add new, remove deleted
        Map<String, CartItemEntity> existingItemsById = existing.getItems().stream()
                .filter(i -> i.getId() != null)
                .collect(Collectors.toMap(CartItemEntity::getId, Function.identity()));

        List<CartItemEntity> mergedItems = new ArrayList<>();
        if (updated.getItems() != null) {
            for (CartItemEntity updatedItem : updated.getItems()) {
                CartItemEntity existingItem = updatedItem.getId() != null
                        ? existingItemsById.get(updatedItem.getId()) : null;
                if (existingItem != null) {
                    // Update existing item in-place (preserves version)
                    existingItem.setProductId(updatedItem.getProductId());
                    existingItem.setVariantId(updatedItem.getVariantId());
                    existingItem.setSku(updatedItem.getSku());
                    existingItem.setProductName(updatedItem.getProductName());
                    existingItem.setSupplierId(updatedItem.getSupplierId());
                    existingItem.setQuantity(updatedItem.getQuantity());
                    existingItem.setUnitPrice(updatedItem.getUnitPrice());
                    existingItem.setLineTotal(updatedItem.getLineTotal());
                    existingItem.setAvailable(updatedItem.isAvailable());
                    existingItem.setCart(existing);
                    mergedItems.add(existingItem);
                } else {
                    // New item
                    updatedItem.setCart(existing);
                    mergedItems.add(updatedItem);
                }
            }
        }
        existing.getItems().clear();
        existing.getItems().addAll(mergedItems);

        // Merge activities: match by ID, preserve versions
        Map<String, CartActivityLogEntity> existingActivitiesById = existing.getActivities().stream()
                .filter(a -> a.getId() != null)
                .collect(Collectors.toMap(CartActivityLogEntity::getId, Function.identity()));

        List<CartActivityLogEntity> mergedActivities = new ArrayList<>();
        if (updated.getActivities() != null) {
            for (CartActivityLogEntity updatedAct : updated.getActivities()) {
                CartActivityLogEntity existingAct = updatedAct.getId() != null
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

    private CartItem toItemModel(CartItemEntity entity, String currency) {
        if (entity == null) return null;
        CartItem model = new CartItem();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setVariantId(entity.getVariantId());
        model.setSku(entity.getSku());
        model.setProductName(entity.getProductName());
        model.setSupplierId(entity.getSupplierId());
        model.setQuantity(entity.getQuantity());
        model.setUnitPrice(Money.of(entity.getUnitPrice(), currency));
        model.setLineTotal(Money.of(entity.getLineTotal(), currency));
        model.setAvailable(entity.isAvailable());
        return model;
    }

    private CartActivityLogEntity toActivityEntity(CartActivityLog model) {
        if (model == null) return null;
        CartActivityLogEntity entity = new CartActivityLogEntity();
        entity.setId(model.getId());
        entity.setActivityName(model.activityName);
        entity.setActivitySuccess(model.activitySuccess);
        entity.setActivityComment(model.activityComment);
        return entity;
    }

    private CartActivityLog toActivityModel(CartActivityLogEntity entity) {
        if (entity == null) return null;
        CartActivityLog model = new CartActivityLog();
        model.setId(entity.getId());
        model.activityName = entity.getActivityName();
        model.activitySuccess = entity.isActivitySuccess();
        model.activityComment = entity.getActivityComment();
        return model;
    }
}
