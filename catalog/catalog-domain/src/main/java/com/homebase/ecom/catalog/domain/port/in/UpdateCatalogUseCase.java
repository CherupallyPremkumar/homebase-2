package com.homebase.ecom.catalog.domain.port.in;

public interface UpdateCatalogUseCase {
    void syncCatalogItem(String offerId, String productId);
}
