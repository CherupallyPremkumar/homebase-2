package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.service.DynamicRuleEvaluator;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor: Takes a CatalogItem, checks it against ALL active dynamic collections,
 * and returns the list of mappings to create.
 *
 * Uses CatalogItem (our read model) instead of ProductCatalogDetails — no
 * cross-service calls needed during batch processing.
 */
@Component
public class CollectionRuleProcessor implements ItemProcessor<CatalogItem, List<CollectionProductMapping>> {

    private final CollectionRepository collectionRepository;
    private final DynamicRuleEvaluator ruleEvaluator;

    private List<Collection> cachedCollections;

    @Autowired
    public CollectionRuleProcessor(CollectionRepository collectionRepository, DynamicRuleEvaluator ruleEvaluator) {
        this.collectionRepository = collectionRepository;
        this.ruleEvaluator = ruleEvaluator;
    }

    @Override
    public List<CollectionProductMapping> process(CatalogItem item) throws Exception {
        if (cachedCollections == null) {
            cachedCollections = collectionRepository.findAllActiveDynamicCollections();
        }

        List<CollectionProductMapping> mappings = new ArrayList<>();
        for (Collection collection : cachedCollections) {
            if (ruleEvaluator.matches(collection.getRuleExpression(), item)) {
                CollectionProductMapping mapping = new CollectionProductMapping();
                mapping.setCollectionId(collection.getId());
                mapping.setProductId(item.getProductId());
                mapping.setAddedBy("BATCH_JOB");
                mappings.add(mapping);
            }
        }
        return mappings;
    }
}
