package com.homebase.ecom.catalog.configuration;

import com.homebase.ecom.catalog.service.CatalogPolicyValidator;
import com.homebase.ecom.catalog.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.catalog.port.client.OfferDataPort;
import com.homebase.ecom.catalog.port.client.ProductDataPort;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.infrastructure.integration.ProductServiceAdapter;
import com.homebase.ecom.catalog.infrastructure.integration.OfferServiceAdapter;
import com.homebase.ecom.catalog.service.event.CatalogEventHandler;
import com.homebase.ecom.catalog.service.impl.CatalogProjectorServiceImpl;
import com.homebase.ecom.catalog.service.impl.DynamicCollectionProjectorImpl;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import com.homebase.ecom.offer.api.OfferService;
import com.homebase.ecom.product.api.ProductService;
import org.chenile.query.service.SearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CatalogConfig {

    @Bean
    public CatalogPolicyValidator catalogPolicyValidator(CconfigClient cconfigClient) {
        return new CatalogPolicyValidator(cconfigClient);
    }

    @Bean
    public DynamicRuleEvaluator dynamicRuleEvaluator() {
        return new DynamicRuleEvaluator();
    }

    // ── Driven Port Adapters (sync calls to other bounded contexts) ──

    @Bean
    public ProductDataPort productDataPort(ProductService productService,
                                            SearchService productSearchService) {
        return new ProductServiceAdapter(productService, productSearchService);
    }

    @Bean
    public OfferDataPort offerDataPort(OfferService offerService) {
        return new OfferServiceAdapter(offerService);
    }

    // ── Use Cases ────────────────────────────────────────────────────

    @Bean
    public UpdateCatalogUseCase catalogProjectorService(ProductDataPort productDataPort,
                                                         OfferDataPort offerDataPort,
                                                         CatalogItemRepository catalogItemRepository) {
        return new CatalogProjectorServiceImpl(productDataPort, offerDataPort, catalogItemRepository);
    }

    @Bean
    public EvaluateDynamicCollectionUseCase evaluateDynamicCollectionUseCase(
            DynamicRuleEvaluator dynamicRuleEvaluator,
            CollectionRepository collectionRepository,
            CatalogItemRepository catalogItemRepository,
            CollectionProductMappingRepository mappingRepository) {
        return new DynamicCollectionProjectorImpl(
                dynamicRuleEvaluator, collectionRepository, catalogItemRepository,
                mappingRepository);
    }

    // ── Driving Adapter (Chenile Kafka event consumer) ───────────────

    @Bean
    CatalogEventHandler catalogEventService() {
        return new CatalogEventHandler();
    }
}
