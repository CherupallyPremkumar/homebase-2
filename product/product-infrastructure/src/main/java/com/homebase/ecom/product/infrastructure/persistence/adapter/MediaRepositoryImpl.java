package com.homebase.ecom.product.infrastructure.persistence.adapter;

import com.homebase.ecom.product.domain.model.MediaAsset;
import com.homebase.ecom.product.domain.port.MediaRepository;
import com.homebase.ecom.product.infrastructure.persistence.entity.MediaAssetEntity;
import com.homebase.ecom.product.infrastructure.persistence.mapper.MediaMapper;
import org.springframework.data.jpa.repository.JpaRepository;

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
    public void save(MediaAsset asset) {
        jpaRepository.save(mapper.toEntity(asset));
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }

    public interface MediaJpaRepository extends JpaRepository<MediaAssetEntity, String> {
    }
}
