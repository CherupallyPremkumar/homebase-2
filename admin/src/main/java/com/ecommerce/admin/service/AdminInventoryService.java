package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminInventoryDto;
import com.ecommerce.inventory.domain.InventoryItem;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminInventoryService {

    private final InventoryService inventoryService;
    private final ProductService productService;

    public AdminInventoryService(InventoryService inventoryService, ProductService productService) {
        this.inventoryService = inventoryService;
        this.productService = productService;
    }

    public List<AdminInventoryDto> getAllInventoryWithProducts() {
        return inventoryService.getAllInventory().stream()
                .map(this::enrichWithProduct)
                .toList();
    }

    public List<AdminInventoryDto> getLowStockItemsWithProducts() {
        return inventoryService.getLowStockItems().stream()
                .map(this::enrichWithProduct)
                .toList();
    }

    public List<AdminInventoryDto> getOutOfStockItemsWithProducts() {
        return inventoryService.getOutOfStockItems().stream()
                .map(this::enrichWithProduct)
                .toList();
    }

    public long getLowStockCount() {
        return inventoryService.getLowStockItems().size();
    }

    public long getOutOfStockCount() {
        return inventoryService.getOutOfStockItems().size();
    }

    public void adjustStock(String productId, Integer newQuantity, String reason) throws Exception {
        log.info("Stock adjustment for product {}: new qty={}, reason={}", productId, newQuantity, reason);
        inventoryService.updateStock(productId, newQuantity);
    }

    public int getLowStockThreshold() {
        return 10;
    }

    /**
     * Create a reorder by restocking inventory to a target quantity.
     * In a real system this would create a purchase order to the supplier.
     * For now, it increases stock by the requested quantity.
     */
    public void createReorder(String productId, Integer quantity) {
        log.info("Reorder created for product {}: quantity={}", productId, quantity);
        InventoryItem item = inventoryService.getInventory(productId);
        int newTotal = item.getQuantity() + quantity;
        inventoryService.updateStock(productId, newTotal);
        log.info("Reorder fulfilled for product {}: stock updated from {} to {}", productId, item.getQuantity(),
                newTotal);
    }

    /**
     * Enriches an InventoryItem with its associated Product details.
     */
    private AdminInventoryDto enrichWithProduct(InventoryItem item) {
        AdminInventoryDto dto = new AdminInventoryDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setQuantity(item.getQuantity());
        dto.setReserved(item.getReserved());
        dto.setAvailableQuantity(item.getAvailableQuantity());
        dto.setLowStockThreshold(item.getLowStockThreshold());

        Optional<Product> product = productService.getProductById(item.getProductId());
        if (product.isPresent()) {
            Product p = product.get();
            dto.setProductName(p.getName());
            dto.setProductCategory(p.getCategory());
            dto.setProductImageUrl(p.getImageUrl());
            productService.getMainOffer(p.getId()).ifPresent(offer -> dto.setProductPrice(offer.getPrice()));
        } else {
            dto.setProductName("Unknown Product");
            dto.setProductCategory("—");
        }

        return dto;
    }
}
