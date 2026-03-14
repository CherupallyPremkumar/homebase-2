package com.homebase.ecom.checkout.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "inventory-service", url = "${app.services.inventory-url}")
public interface InventoryServiceClient {

    @PostMapping("/api/inventory/reserve")
    void reserveInventory(@RequestBody InventoryReservationRequest request);

    @PostMapping("/api/inventory/release")
    void releaseInventory(@RequestBody InventoryReservationRequest request);

    class InventoryReservationRequest {
        private UUID cartId;
        private List<InventoryItem> items;
        // Getters/Setters manually implemented
        public UUID getCartId() { return cartId; }
        public void setCartId(UUID cartId) { this.cartId = cartId; }
        public List<InventoryItem> getItems() { return items; }
        public void setItems(List<InventoryItem> items) { this.items = items; }
    }

    class InventoryItem {
        private String productId;
        private int quantity;
        public String getProductId() { return productId; }
        public void setProductId(String productId) { this.productId = productId; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
