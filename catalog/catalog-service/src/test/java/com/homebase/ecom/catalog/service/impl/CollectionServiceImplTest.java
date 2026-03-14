package com.homebase.ecom.catalog.service.impl;

import com.homebase.ecom.catalog.model.Collection;
import com.homebase.ecom.catalog.model.CollectionType;
import com.homebase.ecom.catalog.repository.CollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CollectionServiceImpl Tests")
class CollectionServiceImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @InjectMocks
    private CollectionServiceImpl collectionService;

    @Test
    @DisplayName("Should create collection with valid data")
    void createCollection_ValidData_SavesCollection() {
        // Arrange
        Collection collection = new Collection();
        collection.setName("Under $50");
        collection.setSlug("under-50");
        collection.setType(CollectionType.DYNAMIC);
        collection.setRuleExpression("price < 50.00");
        collection.setAutoUpdate(true);

        when(collectionRepository.save(any(Collection.class))).thenReturn(collection);

        // Act
        Collection result = collectionService.createCollection(collection);

        // Assert
        assertThat(result).isNotNull();
        verify(collectionRepository).save(collection);
    }

    @Test
    @DisplayName("Should update existing collection fields")
    void updateCollection_ExistingCollection_UpdatesFields() {
        // Arrange
        Collection existing = new Collection();
        existing.setId("coll-001");
        existing.setName("Old Name");
        existing.setSlug("old-slug");

        Collection updates = new Collection();
        updates.setName("New Name");
        updates.setSlug("new-slug");
        updates.setDescription("New Description");
        updates.setType(CollectionType.FEATURED);
        updates.setActive(false);

        when(collectionRepository.findById("coll-001")).thenReturn(Optional.of(existing));
        when(collectionRepository.save(any(Collection.class))).thenReturn(existing);

        // Act
        Collection result = collectionService.updateCollection("coll-001", updates);

        // Assert
        verify(collectionRepository).save(argThat(coll -> coll.getName().equals("New Name") &&
                coll.getSlug().equals("new-slug") &&
                coll.getDescription().equals("New Description") &&
                coll.getType() == CollectionType.FEATURED &&
                coll.getActive() == false));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent collection")
    void updateCollection_NonExistent_ThrowsException() {
        // Arrange
        when(collectionRepository.findById("coll-999")).thenReturn(Optional.empty());

        Collection updates = new Collection();
        updates.setName("Test");

        // Act & Assert
        assertThatThrownBy(() -> collectionService.updateCollection("coll-999", updates))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("Should get collection by ID")
    void getCollectionById_ExistingId_ReturnsCollection() {
        // Arrange
        Collection collection = new Collection();
        collection.setId("coll-001");
        collection.setName("Test Collection");

        when(collectionRepository.findById("coll-001")).thenReturn(Optional.of(collection));

        // Act
        Optional<Collection> result = collectionService.getCollectionById("coll-001");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Test Collection");
    }

    @Test
    @DisplayName("Should return empty when collection not found")
    void getCollectionById_NonExistent_ReturnsEmpty() {
        // Arrange
        when(collectionRepository.findById("coll-999")).thenReturn(Optional.empty());

        // Act
        Optional<Collection> result = collectionService.getCollectionById("coll-999");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should get only dynamic collections")
    void getDynamicCollections_ReturnsOnlyDynamicTypes() {
        // Arrange
        Collection dynamic1 = new Collection();
        dynamic1.setType(CollectionType.DYNAMIC);
        dynamic1.setAutoUpdate(true);

        Collection dynamic2 = new Collection();
        dynamic2.setType(CollectionType.DYNAMIC);
        dynamic2.setAutoUpdate(true);

        when(collectionRepository.findAllActiveDynamicCollections())
                .thenReturn(Arrays.asList(dynamic1, dynamic2));

        // Act
        List<Collection> result = collectionService.getDynamicCollections();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(c -> c.getType() == CollectionType.DYNAMIC);
    }

    @Test
    @DisplayName("Should soft delete collection by setting active to false")
    void deleteCollection_SoftDelete_SetsActiveFalse() {
        // Arrange
        Collection collection = new Collection();
        collection.setId("coll-001");
        collection.setActive(true);

        when(collectionRepository.findById("coll-001")).thenReturn(Optional.of(collection));
        when(collectionRepository.save(any(Collection.class))).thenReturn(collection);

        // Act
        collectionService.deleteCollection("coll-001");

        // Assert
        verify(collectionRepository).save(argThat(coll -> coll.getActive() == false));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent collection")
    void deleteCollection_NonExistent_ThrowsException() {
        // Arrange
        when(collectionRepository.findById("coll-999")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> collectionService.deleteCollection("coll-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }
}
