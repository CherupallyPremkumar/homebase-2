package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionType;
import com.homebase.ecom.product.dto.ProductCatalogDetails;
import com.homebase.ecom.catalog.domain.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionRuleProcessorTest {

    @Mock
    private DynamicRuleEvaluator ruleEvaluator;

    @Mock
    private com.homebase.ecom.catalog.repository.CollectionRepository collectionRepository;

    @InjectMocks
    private CollectionRuleProcessor processor;

    private Collection collection;
    private ProductCatalogDetails product;

    @BeforeEach
    void setUp() {
        collection = new Collection();
        collection.setId("col-1");
        collection.setName("Budget Items");
        collection.setType(CollectionType.DYNAMIC);
        collection.setRuleExpression("price < 50.00");

        product = new ProductCatalogDetails();
        product.setProductId("prod-1");
        product.setName("Test Product");
        product.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("35.00"), "USD"));
        product.setActive(true);
        
        // Mock default call in processor if needed, but processor loads lazily
        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(List.of(collection));
    }

    @Test
    @DisplayName("Should process product that matches collection rule")
    void process_ProductMatchesRule_ReturnsMapping() throws Exception {
        // Arrange
        when(ruleEvaluator.matches(collection.getRuleExpression(), product)).thenReturn(true);

        // Act
        List<CollectionProductMapping> result = processor.process(product);

        // Assert
        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCollectionId()).isEqualTo("col-1");
        assertThat(result.get(0).getProductId()).isEqualTo("prod-1");
        
        verify(ruleEvaluator).matches(collection.getRuleExpression(), product);
    }

    @Test
    @DisplayName("Should return empty list when product does not match any rule")
    void process_ProductDoesNotMatch_ReturnsEmptyList() throws Exception {
        // Arrange
        when(ruleEvaluator.matches(collection.getRuleExpression(), product)).thenReturn(false);

        // Act
        List<CollectionProductMapping> result = processor.process(product);

        // Assert
        assertThat(result).isEmpty();
        
        verify(ruleEvaluator).matches(collection.getRuleExpression(), product);
    }

    @Test
    @DisplayName("Should handle price boundary conditions")
    void process_PriceBoundary_EvaluatesCorrectly() throws Exception {
        // Arrange
        product.setPrice(new com.homebase.ecom.shared.Money(new java.math.BigDecimal("50.00"), "USD"));
        collection.setRuleExpression("price <= 50.00");
        when(ruleEvaluator.matches(collection.getRuleExpression(), product)).thenReturn(true);

        // Act
        List<CollectionProductMapping> result = processor.process(product);

        // Assert
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle multiple matching collections")
    void process_MultipleMatches_ReturnsAllMappings() throws Exception {
        // Arrange
        Collection collection2 = new Collection();
        collection2.setId("col-2");
        collection2.setRuleExpression("active == true");
        
        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(List.of(collection, collection2));
        when(ruleEvaluator.matches(collection.getRuleExpression(), product)).thenReturn(true);
        when(ruleEvaluator.matches(collection2.getRuleExpression(), product)).thenReturn(true);

        // Act
        List<CollectionProductMapping> result = processor.process(product);

        // Assert
        assertThat(result).hasSize(2);
    }
}
