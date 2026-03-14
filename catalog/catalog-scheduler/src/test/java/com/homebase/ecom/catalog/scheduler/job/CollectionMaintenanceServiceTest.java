package com.homebase.ecom.catalog.scheduler.job;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import com.homebase.ecom.catalog.repository.ProductServiceClient;
import com.homebase.ecom.catalog.domain.service.DynamicRuleEvaluator;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CollectionMaintenanceService Tests")
class CollectionMaintenanceServiceTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private CollectionProductMappingRepository mappingRepository;

    @Mock
    private ProductServiceClient productServiceClient;

    @Mock
    private DynamicRuleEvaluator ruleEvaluator;

    @InjectMocks
    private CollectionMaintenanceService maintenanceService;

    private Collection dynamicCollection;
    private ProductCatalogDetails product;

    @BeforeEach
    void setUp() {
        dynamicCollection = new Collection();
        dynamicCollection.setId("col-dynamic");
        dynamicCollection.setName("Dynamic Collection");
        dynamicCollection.setRuleExpression("price < 50.00");
        dynamicCollection.setActive(true);

        product = new ProductCatalogDetails();
        product.setProductId("prod-001");
        product.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("35.00"), "USD"));
    }

    @Test
    @DisplayName("Should refresh dynamic collections")
    void refreshDynamicCollections_ValidCollections_ProcessesThem() {
        // Arrange
        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(Arrays.asList(dynamicCollection));
        when(productServiceClient.getAllProducts()).thenReturn(Arrays.asList(product));
        when(ruleEvaluator.matches(any(), any())).thenReturn(true);

        // Act
        maintenanceService.refreshDynamicCollections();

        // Assert
        verify(collectionRepository).findAllActiveDynamicCollections();
        verify(productServiceClient).getAllProducts();
        verify(mappingRepository).deleteByCollectionId(dynamicCollection.getId());
        verify(mappingRepository).save(any());
    }

    @Test
    @DisplayName("Should handle empty collection list")
    void refreshDynamicCollections_NoCollections_DoesNothing() {
        // Arrange
        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(Collections.emptyList());

        // Act
        maintenanceService.refreshDynamicCollections();

        // Assert
        verify(collectionRepository).findAllActiveDynamicCollections();
        verify(productServiceClient, never()).getAllProducts();
        verify(mappingRepository, never()).deleteByCollectionId(any());
    }
}
