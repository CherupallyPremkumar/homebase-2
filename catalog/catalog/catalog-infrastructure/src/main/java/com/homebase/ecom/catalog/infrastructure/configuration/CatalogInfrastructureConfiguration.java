package com.homebase.ecom.catalog.infrastructure.configuration;

import com.homebase.ecom.catalog.infrastructure.integration.OfferServiceAdapter;
import com.homebase.ecom.catalog.infrastructure.integration.ProductServiceAdapter;
import com.homebase.ecom.catalog.port.client.OfferDataPort;
import com.homebase.ecom.catalog.port.client.ProductDataPort;
import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.product.api.ProductService;
import org.chenile.query.service.SearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Infrastructure layer: wires adapters to domain ports for the Catalog bounded context.
 *
 * Each adapter is an Anti-Corruption Layer (ACL) translating external BC models
 * to catalog-internal snapshots.
 * No @Component/@Service -- all beans declared explicitly via @Bean.
 */
@Configuration
public class CatalogInfrastructureConfiguration {

    @Bean("catalogProductDataPort")
    ProductDataPort catalogProductDataPort(ProductService productService,
                                           @Qualifier("productSearchServiceClient") SearchService productSearchService) {
        return new ProductServiceAdapter(productService, productSearchService);
    }

    @Bean("catalogOfferDataPort")
    OfferDataPort catalogOfferDataPort(OfferService offerService) {
        return new OfferServiceAdapter(offerService);
    }
}
