package com.homebase.ecom.checkout.service.port;

import java.util.UUID;

public interface InventoryClient {
    void reserveInventory(UUID cartId);
    void releaseInventory(UUID cartId);
}
