package com.homebase.ecom.cart.client;

import com.homebase.ecom.cart.dto.CartDto;
import org.chenile.base.response.GenericResponse;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Feign client for the Cart service.
 * This provides a type-safe way for other microservices to interact with the Cart module.
 */
@FeignClient(name = "cart-service", url = "${cart.service.url:http://cart-service}")
public interface CartClient {

    /**
     * Retrieves a cart by its ID.
     */
    @GetMapping("/cart/{id}")
    GenericResponse<StateEntityServiceResponse<CartDto>> getCart(@PathVariable("id") String id);

    /**
     * Creates a new cart. 
     * Note: While the controller takes a domain Cart, we use CartDto here for API decoupling.
     */
    @PostMapping("/cart")
    GenericResponse<StateEntityServiceResponse<CartDto>> createCart(@RequestBody CartDto cartDto);

    /**
     * Triggers a state transition for a cart.
     */
    @PatchMapping("/cart/{id}/{eventId}")
    GenericResponse<StateEntityServiceResponse<CartDto>> processById(
            @PathVariable("id") String id,
            @PathVariable("eventId") String eventId,
            @RequestBody Object payload);

    /**
     * Convenience method to add an item to the cart.
     */
    @PatchMapping("/cart/{id}/addItem")
    GenericResponse<StateEntityServiceResponse<CartDto>> addItem(
            @PathVariable("id") String id,
            @RequestBody com.homebase.ecom.cart.dto.AddItemCartPayload payload);

    /**
     * Convenience method to remove an item from the cart.
     */
    @PatchMapping("/cart/{id}/removeItem")
    GenericResponse<StateEntityServiceResponse<CartDto>> removeItem(
            @PathVariable("id") String id,
            @RequestBody com.homebase.ecom.cart.dto.RemoveItemCartPayload payload);
}
