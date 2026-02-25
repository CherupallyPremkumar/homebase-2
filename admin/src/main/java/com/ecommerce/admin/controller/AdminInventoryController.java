package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.AdminInventoryDto;
import com.ecommerce.admin.service.AdminInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/admin/inventory")
public class AdminInventoryController {

    private final AdminInventoryService inventoryService;

    public AdminInventoryController(AdminInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Inventory list with stock levels and product details
     */
    @GetMapping
    public String listInventory(
            @RequestParam(required = false) String status,
            Model model) {

        List<AdminInventoryDto> inventory;

        if ("low".equals(status)) {
            inventory = inventoryService.getLowStockItemsWithProducts();
        } else if ("out".equals(status)) {
            inventory = inventoryService.getOutOfStockItemsWithProducts();
        } else {
            inventory = inventoryService.getAllInventoryWithProducts();
        }

        model.addAttribute("inventory", inventory);
        model.addAttribute("status", status);
        model.addAttribute("lowStockCount", inventoryService.getLowStockCount());
        model.addAttribute("outOfStockCount", inventoryService.getOutOfStockCount());

        return "admin/inventory/list";
    }

    /**
     * Adjust stock for a product
     */
    @PostMapping("/{productId}/adjust")
    public String adjustStock(
            @PathVariable String productId,
            @RequestParam Integer newQuantity,
            @RequestParam String reason) {

        try {
            inventoryService.adjustStock(productId, newQuantity, reason);
            return "redirect:/admin/inventory?success=Stock adjusted";
        } catch (Exception e) {
            return "redirect:/admin/inventory?error=" + e.getMessage();
        }
    }

    /**
     * Low stock alerts
     */
    @GetMapping("/alerts/low-stock")
    public String lowStockAlerts(Model model) {

        var lowStockItems = inventoryService.getLowStockItemsWithProducts();
        var threshold = inventoryService.getLowStockThreshold();

        model.addAttribute("items", lowStockItems);
        model.addAttribute("threshold", threshold);

        return "admin/inventory/low-stock-alerts";
    }

    /**
     * Create reorder
     */
    @PostMapping("/{productId}/reorder")
    public String createReorder(
            @PathVariable String productId,
            @RequestParam Integer quantity) {

        inventoryService.createReorder(productId, quantity);
        return "redirect:/admin/inventory?success=Reorder created";
    }
}
