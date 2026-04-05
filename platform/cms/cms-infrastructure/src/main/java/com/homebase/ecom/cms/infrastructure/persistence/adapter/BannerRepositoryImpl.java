package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.Banner;
import com.homebase.ecom.cms.model.port.BannerRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.BannerEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BannerRepositoryImpl implements BannerRepository {

    private final BannerJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public BannerRepositoryImpl(BannerJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Banner> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Banner> findByPosition(String position) {
        return jpaRepository.findByPositionAndActiveTrue(position).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Banner> findAllActive() {
        return jpaRepository.findAllByActiveTrue().stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Banner save(Banner banner) {
        BannerEntity entity = mapper.toEntity(banner);
        BannerEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
