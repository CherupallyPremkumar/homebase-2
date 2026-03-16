package com.homebase.ecom.review.infrastructure.persistence.adapter;

import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, String> {
    List<ReviewEntity> findByProductId(String productId);
    List<ReviewEntity> findByCustomerId(String customerId);
}
