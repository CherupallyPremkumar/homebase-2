package com.homebase.ecom.checkout.infrastructure.client.adapter;

import com.homebase.ecom.checkout.infrastructure.client.InventoryServiceClient;
import com.homebase.ecom.checkout.service.port.InventoryClient;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class InventoryClientAdapter implements InventoryClient {
    private final InventoryServiceClient inventoryServiceClient;

    public InventoryClientAdapter(InventoryServiceClient inventoryServiceClient) {
        this.inventoryServiceClient = inventoryServiceClient;
    }

    @Override
    public void reserveInventory(UUID cartId) {
        InventoryServiceClient.InventoryReservationRequest request = new InventoryServiceClient.InventoryReservationRequest();
        request.setCartId(cartId);
        inventoryServiceClient.reserveInventory(request);
    }

    @Override
    public void releaseInventory(UUID cartId) {
        InventoryServiceClient.InventoryReservationRequest request = new InventoryServiceClient.InventoryReservationRequest();
        request.setCartId(cartId);
        inventoryServiceClient.releaseInventory(request);
    }
}
