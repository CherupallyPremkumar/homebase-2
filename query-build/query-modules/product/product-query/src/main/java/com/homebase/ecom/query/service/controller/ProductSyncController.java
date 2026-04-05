package com.homebase.ecom.query.service.controller;

import com.homebase.ecom.query.service.sync.ProductCatalogSyncService;
import org.chenile.base.response.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Manual sync trigger for platform admins.
 * Allows on-demand refresh of OLAP product tables without waiting for nightly batch.
 *
 * POST /admin/sync/products      → full sync (all 3 tables)
 * POST /admin/sync/products/catalog   → product_catalog_flat only
 * POST /admin/sync/products/variants  → product_variant_flat only
 * POST /admin/sync/products/categories → category_config_flat only
 */
@RestController
@RequestMapping("/admin/sync/products")
public class ProductSyncController {

    private final ProductCatalogSyncService syncService;

    public ProductSyncController(ProductCatalogSyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<String>> syncAll() {
        long start = System.currentTimeMillis();
        syncService.syncAll();
        long duration = System.currentTimeMillis() - start;
        var response = new GenericResponse<String>();
        response.setData("Product catalog OLAP sync completed in " + duration + "ms");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/catalog")
    public ResponseEntity<GenericResponse<String>> syncCatalog() {
        syncService.syncProductCatalogFlat();
        var response = new GenericResponse<String>();
        response.setData("product_catalog_flat synced");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/variants")
    public ResponseEntity<GenericResponse<String>> syncVariants() {
        syncService.syncProductVariantFlat();
        var response = new GenericResponse<String>();
        response.setData("product_variant_flat synced");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/categories")
    public ResponseEntity<GenericResponse<String>> syncCategories() {
        syncService.syncCategoryConfigFlat();
        var response = new GenericResponse<String>();
        response.setData("category_config_flat synced");
        return ResponseEntity.ok(response);
    }
}
