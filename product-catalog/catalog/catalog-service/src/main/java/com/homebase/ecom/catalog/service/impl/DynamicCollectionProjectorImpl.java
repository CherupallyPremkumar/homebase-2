package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.port.in.EvaluateDynamicCollectionUseCase;
import com.homebase.ecom.catalog.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Evaluates dynamic collection rules against CatalogItems (our own read model).
 * No external service calls — rules run against data already projected into catalog.
 */
public class DynamicCollectionProjectorImpl implements EvaluateDynamicCollectionUseCase {

    private final DynamicRuleEvaluator ruleEvaluator;
    private final CollectionRepository collectionRepository;
    private final CatalogItemRepository catalogItemRepository;
    private final CollectionProductMappingRepository mappingRepository;

    public DynamicCollectionProjectorImpl(
            DynamicRuleEvaluator ruleEvaluator,
            CollectionRepository collectionRepository,
            CatalogItemRepository catalogItemRepository,
            CollectionProductMappingRepository mappingRepository) {
        this.ruleEvaluator = ruleEvaluator;
        this.collectionRepository = collectionRepository;
        this.catalogItemRepository = catalogItemRepository;
        this.mappingRepository = mappingRepository;
    }

    @Override
    @Transactional
    public void evaluateItemForAllCollections(String productId) {
        Optional<CatalogItem> catalogItemOpt = catalogItemRepository.findByProductId(productId);
        if (catalogItemOpt.isEmpty()) {
            return;
        }
        CatalogItem item = catalogItemOpt.get();

        for (Collection collection : collectionRepository.findAllActiveDynamicCollections()) {
            boolean matches = ruleEvaluator.matches(collection.getRuleExpression(), item);
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

        List<CatalogItem> allItems = catalogItemRepository.findAll();
        for (CatalogItem item : allItems) {
            boolean matches = ruleEvaluator.matches(collection.getRuleExpression(), item);
            updateMapping(collection.getId(), item, matches);
        }
    }

    private void updateMapping(String collectionId, CatalogItem item, boolean matches) {
        Optional<CollectionProductMapping> existingMapping =
                mappingRepository.findByCollectionAndProduct(collectionId, item.getProductId());

        if (matches && existingMapping.isEmpty()) {
            CollectionProductMapping mapping = new CollectionProductMapping();
            mapping.setCollectionId(collectionId);
            mapping.setProductId(item.getProductId());
            mappingRepository.save(mapping);

            if (!item.getCollectionIds().contains(collectionId)) {
                item.getCollectionIds().add(collectionId);
                catalogItemRepository.save(item);
            }
        } else if (!matches && existingMapping.isPresent()) {
            mappingRepository.deleteById(existingMapping.get().getId());

            if (item.getCollectionIds().contains(collectionId)) {
                item.getCollectionIds().remove(collectionId);
                catalogItemRepository.save(item);
            }
        }
    }
}
