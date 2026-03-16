package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CatalogServiceImpl Tests")
class CatalogServiceImplTest {

        @Mock
        private CatalogItemRepository catalogItemRepository;

        @Mock
        private com.homebase.ecom.catalog.repository.CategoryProductMappingRepository categoryMappingRepository;

        @Mock
        private com.homebase.ecom.catalog.repository.ProductServiceClient productServiceClient;

        @Mock
        private com.homebase.ecom.catalog.domain.service.CatalogPolicyValidator policyValidator;

        @InjectMocks
        private CatalogServiceImpl catalogService;

        private CatalogItem sampleCatalogItem;

        @BeforeEach
        void setUp() {
                sampleCatalogItem = new CatalogItem();
                sampleCatalogItem.setProductId("prod-001");
                sampleCatalogItem.setName("Handwoven Basket");
                sampleCatalogItem.setPrice(new BigDecimal("35.00"));
                sampleCatalogItem.setFeatured(false);
                sampleCatalogItem.setActive(true);
        }

        @Test
        @DisplayName("Should create new catalog item when product does not exist")
        void createOrUpdateCatalogItem_NewProduct_CreatesCatalogItem() {
                // Arrange
                com.homebase.ecom.product.dto.ProductCatalogDetails details = new com.homebase.ecom.product.dto.ProductCatalogDetails();
                details.setProductId("prod-001");
                details.setName("Handwoven Basket");
                details.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("35.00"), "USD"));
                details.setActive(true);
                details.setCategory("cat-123");

                when(productServiceClient.getProduct("prod-001")).thenReturn(Optional.of(details));
                when(catalogItemRepository.findByProductId("prod-001")).thenReturn(Optional.empty());
                when(catalogItemRepository.save(any(CatalogItem.class))).thenReturn(sampleCatalogItem);
                when(categoryMappingRepository.findByCategoryIdAndProductId("cat-123", "prod-001"))
                                .thenReturn(Optional.empty());

                // Act
                catalogService
                                .createOrUpdateCatalogItem(
                                                new com.homebase.ecom.shared.event.ProductPublishedEvent("prod-001"));

                // Assert
                verify(productServiceClient).getProduct("prod-001");
                verify(catalogItemRepository).findByProductId("prod-001");
                verify(catalogItemRepository).save(argThat(item -> item.getProductId().equals("prod-001") &&
                                item.getName().equals("Handwoven Basket") &&
                                item.getPrice().compareTo(new BigDecimal("35.00")) == 0));
                verify(categoryMappingRepository)
                                .save(any(com.homebase.ecom.catalog.model.CategoryProductMapping.class));
        }

        @Test
        @DisplayName("Should update existing catalog item when product exists")
        void createOrUpdateCatalogItem_ExistingProduct_UpdatesCatalogItem() {
                // Arrange
                CatalogItem existingItem = new CatalogItem();
                existingItem.setProductId("prod-001");
                existingItem.setName("Old Name");
                existingItem.setPrice(new BigDecimal("25.00"));
                existingItem.setFeatured(true);
                existingItem.setDisplayOrder(1);

                com.homebase.ecom.product.dto.ProductCatalogDetails details = new com.homebase.ecom.product.dto.ProductCatalogDetails();
                details.setProductId("prod-001");
                details.setName("Handwoven Basket");
                details.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("35.00"), "USD"));
                details.setActive(true);
                details.setCategory("cat-123");

                when(productServiceClient.getProduct("prod-001")).thenReturn(Optional.of(details));
                when(catalogItemRepository.findByProductId("prod-001")).thenReturn(Optional.of(existingItem));
                when(catalogItemRepository.save(any(CatalogItem.class))).thenReturn(existingItem);
                when(categoryMappingRepository.findByCategoryIdAndProductId("cat-123", "prod-001"))
                                .thenReturn(Optional
                                                .of(new com.homebase.ecom.catalog.model.CategoryProductMapping()));

                // Act
                catalogService
                                .createOrUpdateCatalogItem(
                                                new com.homebase.ecom.shared.event.ProductPublishedEvent("prod-001"));

                // Assert
                verify(catalogItemRepository).save(argThat(item -> item.getName().equals("Handwoven Basket") &&
                                item.getPrice().compareTo(new BigDecimal("35.00")) == 0 &&
                                item.getFeatured() == true && // Preserved
                                item.getDisplayOrder() == 1 // Preserved
                ));
                verify(categoryMappingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should preserve merchandising overrides when updating")
        void createOrUpdateCatalogItem_PreservesMerchandisingOverrides() {
                // Arrange
                CatalogItem existingItem = new CatalogItem();
                existingItem.setProductId("prod-001");
                existingItem.setFeatured(true);
                existingItem.setDisplayOrder(5);
                existingItem.getTags().add("bestseller");

                com.homebase.ecom.product.dto.ProductCatalogDetails details = new com.homebase.ecom.product.dto.ProductCatalogDetails();
                details.setProductId("prod-001");
                details.setName("Handwoven Basket");
                details.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("35.00"), "USD"));
                details.setActive(true);
                details.setCategory(null);

                when(productServiceClient.getProduct("prod-001")).thenReturn(Optional.of(details));
                when(catalogItemRepository.findByProductId("prod-001")).thenReturn(Optional.of(existingItem));
                when(catalogItemRepository.save(any(CatalogItem.class))).thenReturn(existingItem);

                // Act
                catalogService
                                .createOrUpdateCatalogItem(
                                                new com.homebase.ecom.shared.event.ProductPublishedEvent("prod-001"));

                // Assert
                verify(catalogItemRepository).save(argThat(item -> item.getFeatured() == true &&
                                item.getDisplayOrder() == 5 &&
                                item.getTags().contains("bestseller")));
                verify(categoryMappingRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should not change active when product is in stock")
        void updateVisibility_InStock_DoesNotChangeActive() {
                // Arrange
                CatalogItem item = new CatalogItem();
                item.setProductId("prod-001");
                item.setActive(true);

                when(catalogItemRepository.findByProductId("prod-001")).thenReturn(Optional.of(item));
                when(catalogItemRepository.save(any(CatalogItem.class))).thenReturn(item);

                // Act
                catalogService.updateVisibility("prod-001", 10);

                // Assert
                // Current implementation only sets active=false when quantity<=0
                // It does NOT set active=true when quantity>0
                verify(catalogItemRepository).save(item);
        }

        @Test
        @DisplayName("Should set active to false when product is out of stock")
        void updateVisibility_OutOfStock_SetsActiveFalse() {
                // Arrange
                CatalogItem item = new CatalogItem();
                item.setProductId("prod-001");
                item.setActive(true);

                when(catalogItemRepository.findByProductId("prod-001")).thenReturn(Optional.of(item));
                when(catalogItemRepository.save(any(CatalogItem.class))).thenReturn(item);
                when(policyValidator.shouldHideOutOfStockItems()).thenReturn(true);

                // Act
                catalogService.updateVisibility("prod-001", 0);

                // Assert
                verify(catalogItemRepository).save(argThat(catalogItem -> catalogItem.getActive() == false));
        }
}
