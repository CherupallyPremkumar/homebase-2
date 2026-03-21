package com.homebase.ecom.cart.service.mapper;

import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.dto.CartItemDto;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;

import java.util.stream.Collectors;

public class CartDtoMapper {

    public CartDto toDto(Cart cart) {
        if (cart == null) return null;

        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setCustomerId(cart.getCustomerId());
        dto.setSessionId(cart.getSessionId());
        dto.setSubtotal(cart.getSubtotal().getAmount());
        dto.setCurrency(cart.getCurrency());
        dto.setCouponCodes(cart.getCouponCodes());
        dto.setDiscountAmount(cart.getDiscountAmount().getAmount());
        dto.setTotal(cart.getTotal().getAmount());
        dto.setExpiresAt(cart.getExpiresAt());
        dto.setNotes(cart.getNotes());
        dto.setOrderId(cart.getOrderId());
        dto.setPriceChanged(cart.isPriceChanged());
        dto.setDescription(cart.description);
        if (cart.getCurrentState() != null) {
            dto.setCurrentState(cart.getCurrentState().getStateId());
        }

        if (cart.getItems() != null) {
            dto.setItems(cart.getItems().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public CartItemDto toDto(CartItem item) {
        if (item == null) return null;

        CartItemDto dto = new CartItemDto();
        dto.setProductId(item.getProductId());
        dto.setVariantId(item.getVariantId());
        dto.setSku(item.getSku());
        dto.setProductName(item.getProductName());
        dto.setSupplierId(item.getSupplierId());
        dto.setQuantity(item.getQuantity());
        dto.setUnitPrice(item.getUnitPrice().getAmount());
        dto.setLineTotal(item.getLineTotal().getAmount());
        dto.setAvailable(item.isAvailable());
        dto.setSavedForLater(item.isSavedForLater());
        dto.setImageUrl(item.getImageUrl());
        dto.setOriginalPrice(item.getOriginalPrice() != null ? item.getOriginalPrice().getAmount() : null);

        return dto;
    }
}
