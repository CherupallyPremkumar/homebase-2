package com.homebase.ecom.catalog.service.listener;

import com.homebase.ecom.catalog.domain.port.in.CatalogService;
import com.homebase.ecom.product.event.InventoryChangedEvent;
import com.homebase.ecom.product.event.ProductApprovedEvent;
import com.homebase.ecom.shared.event.ProductPublishedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductEventListener Tests")
class ProductEventListenerTest {

    @Mock
    private CatalogService catalogService;

    @InjectMocks
    private ProductEventListener listener;

    @Test
    @DisplayName("Should call catalog service when product approved event received")
    void onProductApproved_ValidEvent_CallsCatalogService() {
        // Arrange
        ProductApprovedEvent event = new ProductApprovedEvent("prod-001");

        // Act
        listener.onProductApproved(event);

        // Assert
        // The listener wraps it in a ProductPublishedEvent
        verify(catalogService).createOrUpdateCatalogItem(argThat(pubEvent -> pubEvent.getProductId().equals("prod-001")));
    }

    @Test
    @DisplayName("Should update visibility when inventory changed")
    void onProductInventoryChanged_ValidEvent_UpdatesVisibility() {
        // Arrange
        InventoryChangedEvent event = new InventoryChangedEvent("prod-001", 10);

        // Act
        listener.onProductInventoryChanged(event);

        // Assert
        verify(catalogService).updateVisibility("prod-001", 10);
    }

    @Test
    @DisplayName("Should handle null product ID gracefully in onProductApproved")
    void onProductApproved_NullProductId_DoesNotCallService() {
        // Arrange
        ProductApprovedEvent event = new ProductApprovedEvent(null);

        // Act
        listener.onProductApproved(event);

        // Assert
        verify(catalogService, never()).createOrUpdateCatalogItem(any());
    }

    @Test
    @DisplayName("Should handle null product ID gracefully in onProductInventoryChanged")
    void onProductInventoryChanged_NullProductId_DoesNotCallService() {
        // Arrange
        InventoryChangedEvent event = new InventoryChangedEvent(null, 10);

        // Act
        listener.onProductInventoryChanged(event);

        // Assert
        verify(catalogService, never()).updateVisibility(anyString(), anyInt());
    }
}
