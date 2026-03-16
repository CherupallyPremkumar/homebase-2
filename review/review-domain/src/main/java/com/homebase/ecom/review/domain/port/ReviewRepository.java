package com.homebase.ecom.review.domain.port;

import com.homebase.ecom.review.model.Review;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for review persistence.
 */
public interface ReviewRepository {
    Optional<Review> findById(String id);
    List<Review> findByProductId(String productId);
    List<Review> findByCustomerId(String customerId);
    void save(Review review);
    void delete(String id);
}
