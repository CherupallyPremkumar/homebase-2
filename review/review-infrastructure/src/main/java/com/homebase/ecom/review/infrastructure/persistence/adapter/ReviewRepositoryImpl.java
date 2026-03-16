package com.homebase.ecom.review.infrastructure.persistence.adapter;

import com.homebase.ecom.review.model.Review;
import com.homebase.ecom.review.domain.port.ReviewRepository;
import com.homebase.ecom.review.infrastructure.persistence.entity.ReviewEntity;
import com.homebase.ecom.review.infrastructure.persistence.mapper.ReviewMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Review> findByProductId(String productId) {
        return jpaRepository.findByProductId(productId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Review> findByCustomerId(String customerId) {
        return jpaRepository.findByCustomerId(customerId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
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
