package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.item.Chunk;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MappingWriterTest {

    @Mock
    private CollectionProductMappingRepository repository;

    @InjectMocks
    private MappingWriter writer;

    @Captor
    private ArgumentCaptor<List<CollectionProductMapping>> mappingsCaptor;

    private CollectionProductMapping mapping1;
    private CollectionProductMapping mapping2;

    @BeforeEach
    void setUp() {
        mapping1 = new CollectionProductMapping();
        mapping1.setCollectionId("col-1");
        mapping1.setProductId("prod-1");
        mapping1.setDisplayOrder(1);

        mapping2 = new CollectionProductMapping();
        mapping2.setCollectionId("col-1");
        mapping2.setProductId("prod-2");
        mapping2.setDisplayOrder(2);
    }

    @Test
    @DisplayName("Should write batch of mappings to database")
    void write_BatchOfMappings_SavesAll() throws Exception {
        // Arrange
        // Chunk contains a list of mappings (because Processor returns a list per product)
        List<CollectionProductMapping> mappingList = Arrays.asList(mapping1, mapping2);
        Chunk<List<CollectionProductMapping>> chunk = new Chunk<>(List.of(mappingList));

        // Act
        writer.write(chunk);

        // Assert
        verify(repository).saveAll(mappingList);
    }

    @Test
    @DisplayName("Should handle empty chunk")
    void write_EmptyChunk_DoesNothing() throws Exception {
        // Arrange
        Chunk<List<CollectionProductMapping>> emptyChunk = new Chunk<>();

        // Act
        writer.write(emptyChunk);

        // Assert
        verify(repository, never()).saveAll(anyList());
    }

    @Test
    @DisplayName("Should handle multiple lists in one chunk")
    void write_MultipleListsInChunk_SavesAll() throws Exception {
        // Arrange
        List<CollectionProductMapping> list1 = List.of(mapping1);
        List<CollectionProductMapping> list2 = List.of(mapping2);
        Chunk<List<CollectionProductMapping>> chunk = new Chunk<>(Arrays.asList(list1, list2));

        // Act
        writer.write(chunk);

        // Assert
        verify(repository).saveAll(list1);
        verify(repository).saveAll(list2);
    }
}
