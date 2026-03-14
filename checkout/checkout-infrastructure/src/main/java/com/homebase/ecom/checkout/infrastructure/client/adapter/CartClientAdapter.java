package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.domain.model.CartSnapshot;
import com.homebase.ecom.checkout.infrastructure.client.CartServiceClient;
import com.homebase.ecom.checkout.service.port.CartClient;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class CartClientAdapter implements CartClient {
    private final CartServiceClient cartServiceClient;

    public CartClientAdapter(CartServiceClient cartServiceClient) {
        this.cartServiceClient = cartServiceClient;
    }

    @Override
    public CartSnapshot lockCart(UUID cartId) {
        return cartServiceClient.lockCart(cartId);
    }

    @Override
    public void unlockCart(UUID cartId) {
        cartServiceClient.unlockCart(cartId);
    }
}
