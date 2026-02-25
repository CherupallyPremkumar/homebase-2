package com.ecommerce.admin.controller;

import com.ecommerce.admin.dto.AdminProductDto;
import com.ecommerce.admin.service.AdminProductService;
import com.ecommerce.product.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService productService;

    public AdminProductController(AdminProductService productService) {
        this.productService = productService;
    }

    /**
     * Products list
     */
    @GetMapping
    public String listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category,
            Model model) {

        Page<Product> products;

        if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category,
                    PageRequest.of(page, size));
        } else {
            products = productService.getAllProducts(PageRequest.of(page, size));
        }

        model.addAttribute("products", products.getContent().stream()
                .map(productService::convertToDto)
                .toList());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("categories", productService.getAllCategories());

        return "admin/products/list";
    }

    /**
     * Create product page
     */
    @GetMapping("/create")
    public String createProductPage(Model model) {
        model.addAttribute("product", new AdminProductDto());
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/products/create";
    }

    /**
     * Save new product
     */
    @PostMapping("/create")
    public String saveProduct(@ModelAttribute AdminProductDto productDto) {

        try {
            productService.createProduct(productDto);
            return "redirect:/admin/products?success=Product created";
        } catch (Exception e) {
            log.error("Error creating product: {}", e.getMessage());
            return "redirect:/admin/products/create?error=" + e.getMessage();
        }
    }

    /**
     * Edit product page
     */
    @GetMapping("/{productId}/edit")
    public String editProductPage(@PathVariable String productId, Model model) {

        Product product = productService.getProductById(productId);
        AdminProductDto productDto = productService.convertToDto(product);

        model.addAttribute("product", productDto);
        model.addAttribute("categories", productService.getAllCategories());

        return "admin/products/edit";
    }

    /**
     * Update product
     */
    @PostMapping("/{productId}/update")
    public String updateProduct(
            @PathVariable String productId,
            @ModelAttribute AdminProductDto productDto) {

        productService.updateProduct(productId, productDto);
        return "redirect:/admin/products?success=Product updated";
    }

    /**
     * Delete product
     */
    @PostMapping("/{productId}/delete")
    public String deleteProduct(@PathVariable String productId) {

        productService.deleteProduct(productId);
        return "redirect:/admin/products?success=Product deleted";
    }

    /**
     * Bulk import products from CSV
     */
    @PostMapping("/import")
    public String importProducts(@RequestParam MultipartFile file) {

        try {
            productService.importProductsFromCSV(file);
            return "redirect:/admin/products?success=Products imported";
        } catch (Exception e) {
            return "redirect:/admin/products?error=" + e.getMessage();
        }
    }
}
