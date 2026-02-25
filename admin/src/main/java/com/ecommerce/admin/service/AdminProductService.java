package com.ecommerce.admin.service;

import com.ecommerce.admin.dto.AdminProductDto;
import com.ecommerce.inventory.service.InventoryService;
import com.ecommerce.product.domain.Product;
import com.ecommerce.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminProductService {

    private final ProductService productService;
    private final InventoryService inventoryService;

    public AdminProductService(ProductService productService, InventoryService inventoryService) {
        this.productService = productService;
        this.inventoryService = inventoryService;
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    public Page<Product> getProductsByCategory(String category, Pageable pageable) {
        List<Product> all = productService.getProductsByCategory(category);
        return new PageImpl<>(all, pageable, all.size());
    }

    public Product getProductById(String productId) {
        return productService.getProductById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void createProduct(AdminProductDto productDto) throws Exception {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setImageUrl(productDto.getImageUrl());
        productService.saveProduct(product, productDto.getPrice());
    }

    public void updateProduct(String productId, AdminProductDto productDto) {
        Product product = getProductById(productId);
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setCategory(productDto.getCategory());
        product.setImageUrl(productDto.getImageUrl());
        productService.saveProduct(product, productDto.getPrice());
    }

    public void deleteProduct(String productId) {
        productService.deleteProduct(productId);
    }

    public List<String> getAllCategories() {
        return productService.getAllProducts().stream()
                .map(Product::getCategory)
                .distinct()
                .toList();
    }

    /**
     * Import products from a CSV file.
     * Expected CSV format: name,description,category,price
     * First row is treated as header and skipped.
     */
    public void importProductsFromCSV(MultipartFile file) throws Exception {
        int imported = 0;
        int skipped = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String headerLine = reader.readLine(); // skip header
            if (headerLine == null) {
                throw new IllegalArgumentException("CSV file is empty");
            }

            String line;
            int lineNum = 1;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty())
                    continue;

                try {
                    String[] parts = line.split(",", -1);
                    if (parts.length < 4) {
                        log.warn("Skipping CSV line {}: insufficient columns", lineNum);
                        skipped++;
                        continue;
                    }

                    String name = parts[0].trim().replaceAll("^\"|\"$", "");
                    String description = parts[1].trim().replaceAll("^\"|\"$", "");
                    String category = parts[2].trim().replaceAll("^\"|\"$", "");
                    BigDecimal price = new BigDecimal(parts[3].trim().replaceAll("^\"|\"$", ""));

                    Product product = new Product();
                    product.setName(name);
                    product.setDescription(description);
                    product.setCategory(category);

                    productService.saveProduct(product, price);
                    imported++;
                } catch (Exception e) {
                    log.warn("Skipping CSV line {}: {}", lineNum, e.getMessage());
                    skipped++;
                }
            }
        }

        log.info("CSV import complete: {} imported, {} skipped", imported, skipped);
    }

    public AdminProductDto convertToDto(Product product) {
        AdminProductDto dto = new AdminProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setImageUrl(product.getImageUrl());
        dto.setStatus(product.getStatus().name());

        productService.getMainOffer(product.getId()).ifPresent(offer -> {
            dto.setPrice(offer.getPrice());
            dto.setActive(offer.getActive());
        });

        try {
            dto.setStock(inventoryService.getInventory(product.getId()).getAvailableQuantity());
        } catch (Exception e) {
            dto.setStock(0);
        }

        return dto;
    }
}
