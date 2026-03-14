package com.homebase.ecom.catalog.domain.port.in;

public interface EvaluateDynamicCollectionUseCase {
    
    /**
     * Evaluates rules for all active dynamic Collections against a specific CatalogItem.
     * Updates CollectionProductMapping if a new match is found, or removes it if it no longer matches.
     * 
     * @param productId The ID of the product/catalog item to evaluate.
     */
    void evaluateItemForAllCollections(String productId);
    
    /**
     * Evaluates a specific Collection's rules against all active CatalogItems.
     * Useful when a new Collection is created or its rules are updated.
     * 
     * @param collectionId The ID of the collection to evaluate.
     */
    void evaluateCollectionForAllItems(String collectionId);
}
