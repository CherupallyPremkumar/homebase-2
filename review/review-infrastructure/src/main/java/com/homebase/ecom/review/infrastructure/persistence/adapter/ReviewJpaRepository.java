package com.homebase.ecom.review.infrastructure.persistence.adapter;

import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, String> {
}
