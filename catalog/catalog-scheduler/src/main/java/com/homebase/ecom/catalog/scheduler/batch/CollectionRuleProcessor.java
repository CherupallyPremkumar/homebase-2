package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import com.homebase.ecom.catalog.domain.service.DynamicRuleEvaluator;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor: Takes a Product, checks it against ALL active collections,
 * and returns the list of mappings to create.
 */
@Component
public class CollectionRuleProcessor implements ItemProcessor<ProductCatalogDetails, List<CollectionProductMapping>> {

    private final CollectionRepository collectionRepository;
    private final DynamicRuleEvaluator ruleEvaluator;
    
    // Cache active collections to avoid DB hit per product
    // In production: use a proper CacheManager or StepExecutionListener to load once per step
    private List<Collection> cachedCollections;

    @Autowired
    public CollectionRuleProcessor(CollectionRepository collectionRepository, DynamicRuleEvaluator ruleEvaluator) {
        this.collectionRepository = collectionRepository;
        this.ruleEvaluator = ruleEvaluator;
    }

    @Override
    public List<CollectionProductMapping> process(ProductCatalogDetails product) throws Exception {
        if (cachedCollections == null) {
            cachedCollections = collectionRepository.findAllActiveDynamicCollections();
        }

        List<CollectionProductMapping> mappings = new ArrayList<>();
        for (Collection collection : cachedCollections) {
            if (ruleEvaluator.matches(collection.getRuleExpression(), product)) {
                CollectionProductMapping mapping = new CollectionProductMapping();
                mapping.setCollectionId(collection.getId());
                mapping.setProductId(product.getProductId());
                mapping.setAddedBy("BATCH_JOB");
                // mapping.setDisplayOrder(...) - could calculation logic be here
                mappings.add(mapping);
            }
        }
        return mappings;
    }
}
