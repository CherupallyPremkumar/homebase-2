package com.homebase.ecom.catalog.infrastructure.integration;

import com.homebase.ecom.catalog.domain.port.out.ProductClientPort;

public class ProductClientAdapter implements ProductClientPort {

    // private final ProductClient feignClient; (to be injected later)

    @Override
    public ProductOverview fetchProductOverview(String productId) {
        // TODO: Call actual product-client (Feign) when available
        // Return dummy data for now
        ProductOverview overview = new ProductOverview();
        overview.setId(productId);
        overview.setName("Placeholder Saree Name from PIM");
        overview.setSlug("placeholder-slug");
        return overview;
    }
}
