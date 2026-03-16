package com.homebase.ecom.cart.service;

import com.homebase.ecom.cart.dto.CartDto;
import com.homebase.ecom.cart.dto.CreateCartPayload;

/**
 * Service interface for Cart operations.
 * Defines cart-specific methods returning CartDto.
 * Domain model (Cart) stays internal to cart-service.
 */
public interface CartService {

    CartDto createCart(CreateCartPayload payload);

    CartDto getCart(String cartId);

    CartDto getActiveCartForCustomer(String customerId);

    CartDto proceedById(String cartId, String eventId, Object payload);
}
