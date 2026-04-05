package com.homebase.ecom.catalog.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import com.homebase.ecom.catalog.infrastructure.persistence.adapter.CatalogItemRepositoryImpl;
import com.homebase.ecom.catalog.infrastructure.persistence.adapter.CategoryProductMappingRepositoryImpl;
import com.homebase.ecom.catalog.infrastructure.persistence.adapter.CategoryRepositoryImpl;
import com.homebase.ecom.catalog.infrastructure.persistence.adapter.CollectionProductMappingRepositoryImpl;
import com.homebase.ecom.catalog.infrastructure.persistence.adapter.CollectionRepositoryImpl;
import com.homebase.ecom.catalog.infrastructure.persistence.mapper.CatalogMapper;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CatalogItemJpaRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CatalogCategoryJpaRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CategoryProductMappingJpaRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CollectionJpaRepository;
import com.homebase.ecom.catalog.infrastructure.persistence.repository.CollectionProductMappingJpaRepository;
import com.homebase.ecom.catalog.port.in.CatalogService;
import com.homebase.ecom.catalog.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.catalog.port.client.OfferDataPort;
import com.homebase.ecom.catalog.port.client.ProductDataPort;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CategoryProductMappingRepository;
import com.homebase.ecom.catalog.repository.CategoryRepository;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.service.CatalogPolicyValidator;
import com.homebase.ecom.catalog.service.CategoryService;
import com.homebase.ecom.catalog.service.CollectionService;
import com.homebase.ecom.catalog.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.service.event.CatalogEventHandler;
import com.homebase.ecom.catalog.service.impl.CatalogProjectorServiceImpl;
import com.homebase.ecom.catalog.service.impl.CatalogServiceImpl;
import com.homebase.ecom.catalog.service.impl.CategoryServiceImpl;
import com.homebase.ecom.catalog.service.impl.CollectionServiceImpl;
import com.homebase.ecom.catalog.service.impl.DynamicCollectionProjectorImpl;
import com.homebase.ecom.cconfig.sdk.CconfigClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central bean configuration for the Catalog bounded context.
 * All beans are declared here -- no @Component/@Service/@Repository on classes.
 * Catalog is a READ MODEL: event-driven materialized view, not an STM module.
 */
@Configuration
public class CatalogConfig {

    // ── Mapper ────────────────────────────────────────────────────────

    @Bean
    public CatalogMapper catalogMapper() {
        return new CatalogMapper();
    }

    // ── Repository Adapters (domain port implementations) ─────────────

    @Bean
    public CatalogItemRepository catalogItemRepository(CatalogItemJpaRepository jpaRepo, CatalogMapper mapper) {
        return new CatalogItemRepositoryImpl(jpaRepo, mapper);
    }

    @Bean("catalogCategoryRepository")
    public CategoryRepository catalogCategoryRepository(CatalogCategoryJpaRepository jpaRepo, CatalogMapper mapper) {
        return new CategoryRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public CollectionRepository collectionRepository(CollectionJpaRepository jpaRepo, CatalogMapper mapper) {
        return new CollectionRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public CategoryProductMappingRepository categoryProductMappingRepository(
            CategoryProductMappingJpaRepository jpaRepo, CatalogMapper mapper) {
        return new CategoryProductMappingRepositoryImpl(jpaRepo, mapper);
    }

    @Bean
    public CollectionProductMappingRepository collectionProductMappingRepository(
            CollectionProductMappingJpaRepository jpaRepo, CatalogMapper mapper) {
        return new CollectionProductMappingRepositoryImpl(jpaRepo, mapper);
    }

    // ── Domain Services ───────────────────────────────────────────────

    @Bean
    public CatalogPolicyValidator catalogPolicyValidator(CconfigClient cconfigClient) {
        return new CatalogPolicyValidator(cconfigClient);
    }

    @Bean
    public DynamicRuleEvaluator dynamicRuleEvaluator() {
        return new DynamicRuleEvaluator();
    }

    // ── Driven Port Adapters (sync calls to other bounded contexts) ──
    // Wired in CatalogInfrastructureConfiguration (catalog-infrastructure module)

    // ── Application Services (use case implementations) ───────────────

    @Bean
    public CatalogService catalogService(CatalogItemRepository catalogItemRepository,
                                          ProductDataPort productDataPort,
                                          CategoryProductMappingRepository categoryMappingRepository,
                                          CatalogPolicyValidator policyValidator) {
        return new CatalogServiceImpl(catalogItemRepository, productDataPort,
                categoryMappingRepository, policyValidator);
    }

    @Bean
    public CategoryService categoryService(@Qualifier("catalogCategoryRepository") CategoryRepository categoryRepository,
                                            CatalogPolicyValidator policyValidator) {
        return new CategoryServiceImpl(categoryRepository, policyValidator);
    }

    @Bean
    public CollectionService collectionService(CollectionRepository collectionRepository,
                                                CatalogPolicyValidator policyValidator) {
        return new CollectionServiceImpl(collectionRepository, policyValidator);
    }

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

    // ── Driving Adapter (event consumer) ──────────────────────────────

    @Bean
    public CatalogEventHandler catalogEventHandler() {
        return new CatalogEventHandler();
    }
}
