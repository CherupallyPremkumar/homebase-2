package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.domain.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.catalog.domain.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.repository.ProductServiceClient;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class DynamicCollectionProjectorImpl implements EvaluateDynamicCollectionUseCase {

    private final DynamicRuleEvaluator ruleEvaluator;
    private final CollectionRepository collectionRepository;
    private final CatalogItemRepository catalogItemRepository;
    private final CollectionProductMappingRepository mappingRepository;
    private final ProductServiceClient productServiceClient; // ACL to get raw PIM data for rules

    public DynamicCollectionProjectorImpl(
            DynamicRuleEvaluator ruleEvaluator,
            CollectionRepository collectionRepository,
            CatalogItemRepository catalogItemRepository,
            CollectionProductMappingRepository mappingRepository,
            ProductServiceClient productServiceClient) {
        this.ruleEvaluator = ruleEvaluator;
        this.collectionRepository = collectionRepository;
        this.catalogItemRepository = catalogItemRepository;
        this.mappingRepository = mappingRepository;
        this.productServiceClient = productServiceClient;
    }

    @Override
    @Transactional
    public void evaluateItemForAllCollections(String productId) {
        // Fetch the raw PIM data required for rule evaluation
        Optional<ProductCatalogDetails> productOpt = productServiceClient.getProduct(productId);
        if (productOpt.isEmpty()) {
            return;
        }
        ProductCatalogDetails productDetails = productOpt.get();

        // Ensure the CatalogItem exists in our storefront context
        Optional<CatalogItem> catalogItemOpt = catalogItemRepository.findByProductId(productId);
        if (catalogItemOpt.isEmpty()) {
            return;
        }
        CatalogItem item = catalogItemOpt.get();

        // Check against all dynamic collections
        for (Collection collection : collectionRepository.findAllActiveDynamicCollections()) {
            boolean matches = ruleEvaluator.matches(collection.getRuleExpression(), productDetails);
            updateMapping(collection.getId(), item, matches);
        }
    }

    @Override
    @Transactional
    public void evaluateCollectionForAllItems(String collectionId) {
        Optional<Collection> collectionOpt = collectionRepository.findById(collectionId);
        if (collectionOpt.isEmpty()) {
            return;
        }
        Collection collection = collectionOpt.get();

        // Fetch all products from PIM to evaluate the new rule
        // (In a real 30k scenario, this should be paginated or streamed)
        for (ProductCatalogDetails productDetails : productServiceClient.getAllProducts()) {
            boolean matches = ruleEvaluator.matches(collection.getRuleExpression(), productDetails);
            
            // Note: In an event-driven system, the CatalogItem projection handles existence.
            catalogItemRepository.findByProductId(productDetails.getProductId()).ifPresent(item -> {
                updateMapping(collection.getId(), item, matches);
            });
        }
    }

    private void updateMapping(String collectionId, CatalogItem item, boolean matches) {
        Optional<CollectionProductMapping> existingMapping = mappingRepository.findByCollectionAndProduct(collectionId, item.getProductId());

        if (matches && existingMapping.isEmpty()) {
            // Rule match: Create the flat mapping and update CatalogItem array
            CollectionProductMapping mapping = new CollectionProductMapping();
            mapping.setCollectionId(collectionId);
            mapping.setProductId(item.getProductId());
            mappingRepository.save(mapping);

            if (!item.getCollectionIds().contains(collectionId)) {
                item.getCollectionIds().add(collectionId);
                catalogItemRepository.save(item);
            }
        } else if (!matches && existingMapping.isPresent()) {
            // Rule fail: Remove the flat mapping and update CatalogItem array
            mappingRepository.deleteById(existingMapping.get().getId());
            
            if (item.getCollectionIds().contains(collectionId)) {
                item.getCollectionIds().remove(collectionId);
                catalogItemRepository.save(item);
            }
        }
    }
}
