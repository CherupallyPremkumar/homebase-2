package com.homebase.ecom.catalog.configuration;

import com.homebase.ecom.catalog.domain.service.CatalogPolicyValidator;
import com.homebase.ecom.catalog.domain.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.domain.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.domain.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.catalog.domain.port.out.OfferClientPort;
import com.homebase.ecom.catalog.domain.port.out.ProductClientPort;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.repository.ProductServiceClient;
import com.homebase.ecom.catalog.infrastructure.integration.OfferClientAdapter;
import com.homebase.ecom.catalog.infrastructure.integration.OfferEventConsumer;
import com.homebase.ecom.catalog.infrastructure.integration.DynamicCollectionEventListener;
import com.homebase.ecom.catalog.infrastructure.integration.ProductClientAdapter;
import com.homebase.ecom.catalog.service.impl.CatalogProjectorServiceImpl;
import com.homebase.ecom.catalog.service.impl.DynamicCollectionProjectorImpl;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
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

    @Bean
    public ProductClientPort productClientPort() {
        return new ProductClientAdapter(); // ACL Port
    }

    @Bean
    public OfferClientPort offerClientPort() {
        return new OfferClientAdapter(); // ACL Port
    }

    @Bean
    public UpdateCatalogUseCase catalogProjectorService(ProductClientPort productClientPort,
            OfferClientPort offerClientPort,
            CatalogItemRepository catalogItemRepository) {
        return new CatalogProjectorServiceImpl(productClientPort, offerClientPort, catalogItemRepository);
    }

    @Bean
    public OfferEventConsumer offerEventConsumer(UpdateCatalogUseCase updateCatalogUseCase) {
        return new OfferEventConsumer(updateCatalogUseCase); // Inbound Adapter
    }

    @Bean
    public EvaluateDynamicCollectionUseCase evaluateDynamicCollectionUseCase(
            DynamicRuleEvaluator dynamicRuleEvaluator,
            CollectionRepository collectionRepository,
            CatalogItemRepository catalogItemRepository,
            CollectionProductMappingRepository mappingRepository,
            ProductServiceClient productServiceClient) {
        return new DynamicCollectionProjectorImpl(
                dynamicRuleEvaluator, collectionRepository, catalogItemRepository, mappingRepository, productServiceClient);
    }

    @Bean
    public DynamicCollectionEventListener dynamicCollectionEventListener(
            EvaluateDynamicCollectionUseCase evaluateDynamicCollectionUseCase) {
        return new DynamicCollectionEventListener(evaluateDynamicCollectionUseCase);
    }
}
