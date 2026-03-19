package com.homebase.ecom.catalog.port.in;

public interface UpdateCatalogUseCase {
    void syncCatalogItem(String offerId, String productId);
}
