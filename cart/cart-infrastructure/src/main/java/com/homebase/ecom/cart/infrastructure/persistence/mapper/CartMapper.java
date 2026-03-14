package com.homebase.ecom.cart.infrastructure.persistence.mapper;

import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import com.homebase.ecom.cart.model.CartActivityLog;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartEntity;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartItemEntity;
import com.homebase.ecom.cart.infrastructure.persistence.entity.CartActivityLogEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartEntity toEntity(Cart model) {
        if (model == null) return null;
        CartEntity entity = new CartEntity();
        entity.setId(model.getId());
        entity.setUserId(model.getUserId());
        entity.setTotalAmount(model.getTotalAmount());
        entity.setShippingAddress(model.getShippingAddress());
        entity.setBillingAddress(model.getBillingAddress());
        entity.setAppliedPromoCode(model.getAppliedPromoCode());
        entity.setDiscountAmount(model.getDiscountAmount());
        entity.setTaxAmount(model.getTaxAmount());
        entity.description = model.description;
        entity.setCurrentState(model.getCurrentState());
        
        if (model.getItems() != null) {
            entity.setItems(model.getItems().stream()
                .map(item -> toItemEntity(item, entity))
                .collect(Collectors.toList()));
        }
        
        // Note: Activities and other fields can be mapped here as needed
        return entity;
    }

    public Cart toModel(CartEntity entity) {
        if (entity == null) return null;
        Cart model = new Cart();
        model.setId(entity.getId());
        model.setUserId(entity.getUserId());
        model.setTotalAmount(entity.getTotalAmount());
        model.setShippingAddress(entity.getShippingAddress());
        model.setBillingAddress(entity.getBillingAddress());
        model.setAppliedPromoCode(entity.getAppliedPromoCode());
        model.setDiscountAmount(entity.getDiscountAmount());
        model.setTaxAmount(entity.getTaxAmount());
        model.description = entity.description;
        model.setCurrentState(entity.getCurrentState());

        if (entity.getItems() != null) {
            model.setItems(entity.getItems().stream()
                .map(this::toItemModel)
                .collect(Collectors.toList()));
        }
        return model;
    }

    private CartItemEntity toItemEntity(CartItem model, CartEntity cartEntity) {
        if (model == null) return null;
        CartItemEntity entity = new CartItemEntity();
        entity.setId(model.getId());
        entity.setCart(cartEntity);
        entity.setProductId(model.getProductId());
        entity.setQuantity(model.getQuantity());
        entity.setPrice(model.getPrice());
        entity.setSellerId(model.getSellerId());
        entity.setStatus(model.getStatus());
        return entity;
    }

    private CartItem toItemModel(CartItemEntity entity) {
        if (entity == null) return null;
        CartItem model = new CartItem();
        model.setId(entity.getId());
        model.setProductId(entity.getProductId());
        model.setQuantity(entity.getQuantity());
        model.setPrice(entity.getPrice());
        model.setSellerId(entity.getSellerId());
        model.setStatus(entity.getStatus());
        return model;
    }
}
