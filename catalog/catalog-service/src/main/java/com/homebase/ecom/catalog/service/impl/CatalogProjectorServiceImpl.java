package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.domain.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.domain.port.out.OfferClientPort;
import com.homebase.ecom.catalog.domain.port.out.ProductClientPort;
import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class CatalogProjectorServiceImpl implements UpdateCatalogUseCase {

    private final ProductClientPort productClient;
    private final OfferClientPort offerClient;
    private final CatalogItemRepository catalogRepository;

    public CatalogProjectorServiceImpl(ProductClientPort productClient, OfferClientPort offerClient, CatalogItemRepository catalogRepository) {
        this.productClient = productClient;
        this.offerClient = offerClient;
        this.catalogRepository = catalogRepository;
    }

    @Override
    @Transactional
    public void syncCatalogItem(String offerId, String productId) {
        // 1. Fetch data from different Bounded Contexts via ACL Ports
        ProductClientPort.ProductOverview productData = productClient.fetchProductOverview(productId);
        OfferClientPort.OfferOverview offerData = offerClient.fetchOfferDetails(offerId);

        // 2. Build or Update the Read Model (CatalogItem)
        CatalogItem item = catalogRepository.findByProductId(productId)
                .orElseGet(() -> {
                    CatalogItem newItem = new CatalogItem();
                    newItem.setProductId(productId);
                    newItem.setVisibilityStartDate(LocalDateTime.now());
                    return newItem;
                });

        item.setName(productData.getName());
        item.setCategoryIds(productData.getCategoryIds());
        
        item.setPrice(offerData.getPrice());
        item.setActive(offerData.isActive());

        // 3. Save to the optimized Storefront DB/Cache
        catalogRepository.save(item);
    }
}
