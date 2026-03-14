package com.homebase.ecom.review.domain.port;

import com.homebase.ecom.review.model.Review;

import java.util.Optional;

public interface ReviewRepository {
    Optional<Review> findById(String id);
    void save(Review review);
    void delete(String id);
}
