package com.homebase.ecom.review.infrastructure.persistence.adapter;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.domain.port.ReviewRepository;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import com.homebase.ecom.review.infrastructure.persistence.mapper.ReviewMapper;

import java.util.Optional;

public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;
    private final ReviewMapper mapper;

    public ReviewRepositoryImpl(ReviewJpaRepository jpaRepository, ReviewMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Review> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public void save(Review review) {
        ReviewEntity entity = mapper.toEntity(review);
        jpaRepository.save(entity);
        review.setId(entity.getId());
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
