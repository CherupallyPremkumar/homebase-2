package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.MediaAsset;
import com.homebase.ecom.product.domain.port.MediaRepository;
import com.homebase.ecom.product.infrastructure.persistence.mapper.MediaMapper;

import java.util.List;
import java.util.Optional;

public class MediaRepositoryImpl implements MediaRepository {

    private final MediaJpaRepository jpaRepository;
    private final MediaMapper mapper;

    public MediaRepositoryImpl(MediaJpaRepository jpaRepository, MediaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<MediaAsset> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<MediaAsset> findAllByIdIn(List<String> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return jpaRepository.findAllById(ids).stream().map(mapper::toModel).toList();
    }

    @Override
    public void save(MediaAsset asset) {
        jpaRepository.save(mapper.toEntity(asset));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
