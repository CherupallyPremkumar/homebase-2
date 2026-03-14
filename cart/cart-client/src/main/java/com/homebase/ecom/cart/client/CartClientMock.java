package com.homebase.ecom.cart.client;

import com.homebase.ecom.cart.dto.CartDto;
import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;

/**
 * Mock implementation of the Cart client for unit testing.
 */
public class CartClientMock implements CartClient {
    
    @Override
    public GenericResponse<StateEntityServiceResponse<CartDto>> getCart(String id) {
        CartDto dto = new CartDto();
        dto.setId(id);
        
        StateEntityServiceResponse<CartDto> sesr = new StateEntityServiceResponse<>();
        sesr.setMutatedEntity(dto);
        
        return new GenericResponse<>(sesr);
    }

    @Override
    public GenericResponse<StateEntityServiceResponse<CartDto>> createCart(CartDto cartDto) {
        StateEntityServiceResponse<CartDto> sesr = new StateEntityServiceResponse<>();
        sesr.setMutatedEntity(cartDto);
        
        return new GenericResponse<>(sesr);
    }

    @Override
    public GenericResponse<StateEntityServiceResponse<CartDto>> processById(String id, String eventId, Object payload) {
        CartDto dto = new CartDto();
        dto.setId(id);
        
        StateEntityServiceResponse<CartDto> sesr = new StateEntityServiceResponse<>();
        sesr.setMutatedEntity(dto);
        
        return new GenericResponse<>(sesr);
    }

    @Override
    public GenericResponse<StateEntityServiceResponse<CartDto>> addItem(String id, com.homebase.ecom.cart.dto.AddItemCartPayload payload) {
        return processById(id, "addItem", payload);
    }

    @Override
    public GenericResponse<StateEntityServiceResponse<CartDto>> removeItem(String id, com.homebase.ecom.cart.dto.RemoveItemCartPayload payload) {
        return processById(id, "removeItem", payload);
    }
}
