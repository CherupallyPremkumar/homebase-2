package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.port.in.UpdateCatalogUseCase;
import com.homebase.ecom.catalog.port.client.OfferDataPort;
import com.homebase.ecom.catalog.port.client.OfferSnapshot;
import com.homebase.ecom.catalog.port.client.ProductDataPort;
import com.homebase.ecom.catalog.port.client.ProductSnapshot;
import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CatalogProjectorServiceImpl implements UpdateCatalogUseCase {

    private static final Logger log = LoggerFactory.getLogger(CatalogProjectorServiceImpl.class);

    private final ProductDataPort productDataPort;
    private final OfferDataPort offerDataPort;
    private final CatalogItemRepository catalogRepository;

    public CatalogProjectorServiceImpl(ProductDataPort productDataPort,
                                        OfferDataPort offerDataPort,
                                        CatalogItemRepository catalogRepository) {
        this.productDataPort = productDataPort;
        this.offerDataPort = offerDataPort;
        this.catalogRepository = catalogRepository;
    }

    @Override
    @Transactional
    public void syncCatalogItem(String offerId, String productId) {
        Optional<ProductSnapshot> productOpt = productDataPort.getProduct(productId);
        Optional<OfferSnapshot> offerOpt = offerDataPort.getOffer(offerId);

        if (productOpt.isEmpty()) {
            log.warn("Product {} not found — skipping catalog sync", productId);
            return;
        }
        ProductSnapshot productData = productOpt.get();

        CatalogItem item = catalogRepository.findByProductId(productId)
                .orElseGet(() -> {
                    CatalogItem newItem = new CatalogItem();
                    newItem.setProductId(productId);
                    newItem.setVisibilityStartDate(LocalDateTime.now());
                    return newItem;
                });

        item.setName(productData.getName());
        item.setDescription(productData.getDescription());
        item.setBrand(productData.getBrand());
        item.setImageUrl(productData.getImageUrl());
        if (productData.getCategory() != null) {
            item.setCategoryIds(List.of(productData.getCategory()));
        }

        offerOpt.ifPresent(offer -> {
            if (offer.getOfferPrice() != null) {
                item.setPrice(offer.getOfferPrice());
            }
            item.setActive(!"SUSPENDED".equals(offer.getStatus()) && !"EXPIRED".equals(offer.getStatus()));
        });

        catalogRepository.save(item);
    }
}
