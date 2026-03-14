package com.homebase.ecom.cart.infrastructure.mapper;

import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.dto.CartItemDto;
import com.homebase.ecom.cart.model.Cart;
import com.homebase.ecom.cart.model.CartItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartDtoMapper {

    public CartDto toDto(Cart cart) {
        if (cart == null) return null;

        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setTotalAmount(cart.getTotalAmount());
        dto.setShippingAddress(cart.getShippingAddress());
        dto.setBillingAddress(cart.getBillingAddress());
        dto.setAppliedPromoCode(cart.getAppliedPromoCode());
        dto.setDiscountAmount(cart.getDiscountAmount());
        dto.setTaxAmount(cart.getTaxAmount());
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
        dto.setCartId(item.getCartId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSellerId(item.getSellerId());
        dto.setStatus(item.getStatus());

        return dto;
    }
}
