package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.service.CollectionService;
import com.homebase.ecom.catalog.service.CatalogPolicyValidator;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final CatalogPolicyValidator policyValidator;

    public CollectionServiceImpl(CollectionRepository collectionRepository, CatalogPolicyValidator policyValidator) {
        this.collectionRepository = collectionRepository;
        this.policyValidator = policyValidator;
    }

    @Override
    @Transactional
    public Collection createCollection(Collection collection) {
        enforceFeaturedPolicies(collection);
        return collectionRepository.save(collection);
    }

    @Override
    @Transactional
    public Collection updateCollection(String id, Collection collection) {
        Collection existing = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found: " + id));

        existing.setName(collection.getName());
        existing.setSlug(collection.getSlug());
        existing.setDescription(collection.getDescription());
        existing.setType(collection.getType());
        existing.setImageUrl(collection.getImageUrl());
        existing.setStartDate(collection.getStartDate());
        existing.setEndDate(collection.getEndDate());
        existing.setActive(collection.getActive());
        existing.setFeatured(collection.getFeatured());
        existing.setDisplayOrder(collection.getDisplayOrder());
        existing.setAutoUpdate(collection.getAutoUpdate());
        existing.setRuleExpression(collection.getRuleExpression());

        enforceFeaturedPolicies(existing);

        return collectionRepository.save(existing);
    }

    private void enforceFeaturedPolicies(Collection collection) {
        if (Boolean.TRUE.equals(collection.getFeatured())) {
            policyValidator.validateFeaturedCollectionImage(collection);
            int currentFeatured = collectionRepository.countByFeaturedTrue();
            if (collection.getId() == null) {
                policyValidator.validateFeaturedCollectionCount(currentFeatured);
            }
        }
    }

    @Override
    public Optional<Collection> getCollectionById(String id) {
        return collectionRepository.findById(id);
    }

    @Override
    public List<Collection> getActiveCollections() {
        return collectionRepository.findByActiveTrue();
    }

    @Override
    public List<Collection> getDynamicCollections() {
        return collectionRepository.findAllActiveDynamicCollections();
    }

    @Override
    @Transactional
    public void deleteCollection(String id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found: " + id));
        collection.setActive(false);
        collectionRepository.save(collection);
    }
}
