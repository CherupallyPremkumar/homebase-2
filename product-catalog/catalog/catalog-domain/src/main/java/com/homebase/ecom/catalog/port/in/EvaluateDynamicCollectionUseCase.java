package com.homebase.ecom.catalog.port.in;

public interface EvaluateDynamicCollectionUseCase {
    void evaluateItemForAllCollections(String productId);
    void evaluateCollectionForAllItems(String collectionId);
}
