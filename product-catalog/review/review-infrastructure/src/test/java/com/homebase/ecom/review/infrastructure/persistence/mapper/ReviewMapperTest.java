package com.homebase.ecom.review.infrastructure.persistence.mapper;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReviewMapper bidirectional mapping between Review domain model
 * and ReviewEntity JPA entity.
 */
class ReviewMapperTest {

    private ReviewMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ReviewMapper();
    }

    // ================================================================
    // toModel tests
    // ================================================================

    @Test
    void toModel_nullEntity_returnsNull() {
        assertNull(mapper.toModel(null));
    }

    @Test
    void toModel_mapsAllFields() {
        ReviewEntity entity = createSampleEntity();

        Review model = mapper.toModel(entity);

        assertNotNull(model);
        assertEquals("review-001", model.getId());
        assertEquals("prod-001", model.getProductId());
        assertEquals("cust-123", model.getCustomerId());
        assertEquals("order-456", model.getOrderId());
        assertEquals(5, model.getRating());
        assertEquals("Great product", model.getTitle());
        assertEquals("Excellent quality and fast shipping", model.getBody());
        assertTrue(model.isVerifiedPurchase());
        assertEquals(10, model.getHelpfulCount());
        assertEquals(2, model.getReportCount());
        assertEquals("Approved by moderator", model.getModeratorNotes());
    }

    @Test
    void toModel_mapsImages() {
        ReviewEntity entity = createSampleEntity();
        entity.setImagesJson("https://img1.jpg|https://img2.jpg|https://img3.jpg");

        Review model = mapper.toModel(entity);

        assertNotNull(model.getImages());
        assertEquals(3, model.getImages().size());
        assertEquals("https://img1.jpg", model.getImages().get(0));
        assertEquals("https://img2.jpg", model.getImages().get(1));
        assertEquals("https://img3.jpg", model.getImages().get(2));
    }

    @Test
    void toModel_emptyImages() {
        ReviewEntity entity = createSampleEntity();
        entity.setImagesJson(null);

        Review model = mapper.toModel(entity);

        assertNotNull(model.getImages());
        assertTrue(model.getImages().isEmpty());
    }

    @Test
    void toModel_mapsActivities() {
        ReviewEntity entity = createSampleEntity();
        entity.addActivity("submitReview", "Review submitted");
        entity.addActivity("publishReview", "Review published");

        Review model = mapper.toModel(entity);

        assertNotNull(model.obtainActivities());
        assertEquals(2, model.obtainActivities().size());
    }

    // ================================================================
    // toEntity tests
    // ================================================================

    @Test
    void toEntity_nullModel_returnsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toEntity_mapsAllFields() {
        Review model = createSampleModel();

        ReviewEntity entity = mapper.toEntity(model);

        assertNotNull(entity);
        assertEquals("review-001", entity.getId());
        assertEquals("prod-001", entity.getProductId());
        assertEquals("cust-123", entity.getCustomerId());
        assertEquals("order-456", entity.getOrderId());
        assertEquals(5, entity.getRating());
        assertEquals("Great product", entity.getTitle());
        assertEquals("Excellent quality and fast shipping", entity.getBody());
        assertTrue(entity.isVerifiedPurchase());
        assertEquals(10, entity.getHelpfulCount());
        assertEquals(2, entity.getReportCount());
        assertEquals("Approved by moderator", entity.getModeratorNotes());
    }

    @Test
    void toEntity_mapsImages() {
        Review model = createSampleModel();
        model.setImages(Arrays.asList("https://img1.jpg", "https://img2.jpg"));

        ReviewEntity entity = mapper.toEntity(model);

        assertNotNull(entity.getImagesJson());
        assertEquals("https://img1.jpg|https://img2.jpg", entity.getImagesJson());
    }

    @Test
    void toEntity_emptyImages() {
        Review model = createSampleModel();
        model.setImages(List.of());

        ReviewEntity entity = mapper.toEntity(model);

        assertNull(entity.getImagesJson());
    }

    // ================================================================
    // Round-trip tests
    // ================================================================

    @Test
    void roundTrip_modelToEntityToModel() {
        Review original = createSampleModel();
        original.setImages(Arrays.asList("https://img1.jpg", "https://img2.jpg"));

        ReviewEntity entity = mapper.toEntity(original);
        Review roundTripped = mapper.toModel(entity);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getProductId(), roundTripped.getProductId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getRating(), roundTripped.getRating());
        assertEquals(original.getTitle(), roundTripped.getTitle());
        assertEquals(original.getBody(), roundTripped.getBody());
        assertEquals(original.isVerifiedPurchase(), roundTripped.isVerifiedPurchase());
        assertEquals(original.getHelpfulCount(), roundTripped.getHelpfulCount());
        assertEquals(original.getReportCount(), roundTripped.getReportCount());
        assertEquals(original.getModeratorNotes(), roundTripped.getModeratorNotes());
        assertEquals(original.getImages().size(), roundTripped.getImages().size());
        assertEquals(original.getImages().get(0), roundTripped.getImages().get(0));
        assertEquals(original.getImages().get(1), roundTripped.getImages().get(1));
    }

    @Test
    void roundTrip_entityToModelToEntity() {
        ReviewEntity original = createSampleEntity();
        original.setImagesJson("https://img1.jpg|https://img2.jpg");

        Review model = mapper.toModel(original);
        ReviewEntity roundTripped = mapper.toEntity(model);

        assertEquals(original.getId(), roundTripped.getId());
        assertEquals(original.getProductId(), roundTripped.getProductId());
        assertEquals(original.getCustomerId(), roundTripped.getCustomerId());
        assertEquals(original.getOrderId(), roundTripped.getOrderId());
        assertEquals(original.getRating(), roundTripped.getRating());
        assertEquals(original.getTitle(), roundTripped.getTitle());
        assertEquals(original.getBody(), roundTripped.getBody());
        assertEquals(original.isVerifiedPurchase(), roundTripped.isVerifiedPurchase());
        assertEquals(original.getHelpfulCount(), roundTripped.getHelpfulCount());
        assertEquals(original.getReportCount(), roundTripped.getReportCount());
        assertEquals(original.getModeratorNotes(), roundTripped.getModeratorNotes());
        assertEquals(original.getImagesJson(), roundTripped.getImagesJson());
    }

    // ================================================================
    // Helper methods
    // ================================================================

    private ReviewEntity createSampleEntity() {
        ReviewEntity entity = new ReviewEntity();
        entity.setId("review-001");
        entity.setProductId("prod-001");
        entity.setCustomerId("cust-123");
        entity.setOrderId("order-456");
        entity.setRating(5);
        entity.setTitle("Great product");
        entity.setBody("Excellent quality and fast shipping");
        entity.setVerifiedPurchase(true);
        entity.setHelpfulCount(10);
        entity.setReportCount(2);
        entity.setModeratorNotes("Approved by moderator");
        return entity;
    }

    private Review createSampleModel() {
        Review model = new Review();
        model.setId("review-001");
        model.setProductId("prod-001");
        model.setCustomerId("cust-123");
        model.setOrderId("order-456");
        model.setRating(5);
        model.setTitle("Great product");
        model.setBody("Excellent quality and fast shipping");
        model.setVerifiedPurchase(true);
        model.setHelpfulCount(10);
        model.setReportCount(2);
        model.setModeratorNotes("Approved by moderator");
        return model;
    }
}
