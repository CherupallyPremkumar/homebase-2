package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.domain.port.in.CatalogService;
import com.homebase.ecom.catalog.domain.service.CatalogPolicyValidator;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import com.homebase.ecom.catalog.repository.CategoryProductMappingRepository;
import com.homebase.ecom.catalog.repository.ProductServiceClient;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogItemRepository catalogItemRepository;
    private final ProductServiceClient productServiceClient;
    private final CategoryProductMappingRepository categoryMappingRepository;
    private final CatalogPolicyValidator policyValidator;

    public CatalogServiceImpl(
            CatalogItemRepository catalogItemRepository,
            ProductServiceClient productServiceClient,
            CategoryProductMappingRepository categoryMappingRepository,
            CatalogPolicyValidator policyValidator) {
        this.catalogItemRepository = catalogItemRepository;
        this.productServiceClient = productServiceClient;
        this.categoryMappingRepository = categoryMappingRepository;
        this.policyValidator = policyValidator;
    }

    @Override
    @Transactional
    public CatalogItem createOrUpdateCatalogItem(ProductPublishedEvent event) {
        String productId = event.getProductId();

        Optional<ProductCatalogDetails> detailsOpt = productServiceClient.getProduct(productId);

        if (detailsOpt.isEmpty()) {
            throw new RuntimeException("Product details for ID " + productId + " not found in external service");
        }
        ProductCatalogDetails details = detailsOpt.get();

        Optional<CatalogItem> existingItemOpt = catalogItemRepository.findByProductId(productId);
        CatalogItem catalogItem;

        if (existingItemOpt.isPresent()) {
            catalogItem = existingItemOpt.get();
        } else {
            catalogItem = new CatalogItem();
            catalogItem.setProductId(productId);
            catalogItem.setFeatured(false);
            catalogItem.setDisplayOrder(999);
            catalogItem.setActive(true);
        }

        catalogItem.setName(details.getName());
        catalogItem.setPrice(details.getPrice() != null ? details.getPrice().getAmount() : BigDecimal.ZERO);

        policyValidator.validateCatalogItemTags(catalogItem.getTags());

        if (details.getActive() != null && !details.getActive()) {
            catalogItem.setActive(false);
        }

        CatalogItem savedItem = catalogItemRepository.save(catalogItem);

        String category = details.getCategory();
        if (category != null && !category.trim().isEmpty()) {
            Optional<com.homebase.ecom.catalog.model.CategoryProductMapping> existingMapping = categoryMappingRepository
                    .findByCategoryIdAndProductId(category, productId);

            if (existingMapping.isEmpty()) {
                com.homebase.ecom.catalog.model.CategoryProductMapping mapping = new com.homebase.ecom.catalog.model.CategoryProductMapping();
                mapping.setCategoryId(category);
                mapping.setProductId(productId);
                mapping.setDisplayOrder(999);
                mapping.setAddedBy("EVENT_PROCESSOR");
                categoryMappingRepository.save(mapping);
            }
        }

        return savedItem;
    }

    @Override
    @Transactional
    public void updateVisibility(String productId, int newQuantity) {
        Optional<CatalogItem> itemOpt = catalogItemRepository.findByProductId(productId);
        if (itemOpt.isPresent()) {
            CatalogItem item = itemOpt.get();

            if (newQuantity <= policyValidator.getLowStockThreshold()) {
                // Future: add low stock badge
            }

            if (newQuantity <= 0 && policyValidator.shouldHideOutOfStockItems()) {
                item.setActive(false);
            } else if (newQuantity > 0 && policyValidator.shouldAutoReactivateOnRestock() && !item.getActive()) {
                item.setActive(true);
            }

            catalogItemRepository.save(item);
        }
    }
}
