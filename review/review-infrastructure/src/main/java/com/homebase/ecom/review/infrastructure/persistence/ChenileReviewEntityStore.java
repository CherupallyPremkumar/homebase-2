package com.homebase.ecom.review.infrastructure.persistence;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.infrastructure.persistence.adapter.ReviewJpaRepository;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import com.homebase.ecom.review.infrastructure.persistence.mapper.ReviewMapper;
import org.chenile.jpautils.store.ChenileJpaEntityStore;

public class ChenileReviewEntityStore extends ChenileJpaEntityStore<Review, ReviewEntity> {

    public ChenileReviewEntityStore(ReviewJpaRepository repository, ReviewMapper mapper) {
        super(repository, (entity) -> mapper.toModel(entity), (model) -> mapper.toEntity(model));
    }
}
