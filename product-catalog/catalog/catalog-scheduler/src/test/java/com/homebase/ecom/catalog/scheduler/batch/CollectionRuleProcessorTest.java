package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionType;
import com.homebase.ecom.catalog.service.DynamicRuleEvaluator;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
    private CatalogItem item;

    @BeforeEach
    void setUp() {
        collection = new Collection();
        collection.setId("col-1");
        collection.setName("Budget Items");
        collection.setType(CollectionType.DYNAMIC);
        collection.setRuleExpression("price < 50.00");

        item = new CatalogItem();
        item.setProductId("prod-1");
        item.setName("Test Product");
        item.setPrice(new BigDecimal("35.00"));
        item.setActive(true);

        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(List.of(collection));
    }

    @Test
    @DisplayName("Should process item that matches collection rule")
    void process_ItemMatchesRule_ReturnsMapping() throws Exception {
        when(ruleEvaluator.matches(collection.getRuleExpression(), item)).thenReturn(true);

        List<CollectionProductMapping> result = processor.process(item);

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).getCollectionId()).isEqualTo("col-1");
        assertThat(result.get(0).getProductId()).isEqualTo("prod-1");

        verify(ruleEvaluator).matches(collection.getRuleExpression(), item);
    }

    @Test
    @DisplayName("Should return empty list when item does not match any rule")
    void process_ItemDoesNotMatch_ReturnsEmptyList() throws Exception {
        when(ruleEvaluator.matches(collection.getRuleExpression(), item)).thenReturn(false);

        List<CollectionProductMapping> result = processor.process(item);

        assertThat(result).isEmpty();

        verify(ruleEvaluator).matches(collection.getRuleExpression(), item);
    }

    @Test
    @DisplayName("Should handle price boundary conditions")
    void process_PriceBoundary_EvaluatesCorrectly() throws Exception {
        item.setPrice(new BigDecimal("50.00"));
        collection.setRuleExpression("price <= 50.00");
        when(ruleEvaluator.matches(collection.getRuleExpression(), item)).thenReturn(true);

        List<CollectionProductMapping> result = processor.process(item);

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("Should handle multiple matching collections")
    void process_MultipleMatches_ReturnsAllMappings() throws Exception {
        Collection collection2 = new Collection();
        collection2.setId("col-2");
        collection2.setRuleExpression("active == true");

        when(collectionRepository.findAllActiveDynamicCollections()).thenReturn(List.of(collection, collection2));
        when(ruleEvaluator.matches(collection.getRuleExpression(), item)).thenReturn(true);
        when(ruleEvaluator.matches(collection2.getRuleExpression(), item)).thenReturn(true);

        List<CollectionProductMapping> result = processor.process(item);

        assertThat(result).hasSize(2);
    }
}
