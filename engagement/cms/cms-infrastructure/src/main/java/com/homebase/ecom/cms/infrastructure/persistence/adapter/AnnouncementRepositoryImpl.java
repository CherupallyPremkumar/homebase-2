package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.Announcement;
import com.homebase.ecom.cms.model.port.AnnouncementRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.AnnouncementEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnouncementRepositoryImpl implements AnnouncementRepository {

    private final AnnouncementJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public AnnouncementRepositoryImpl(AnnouncementJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Announcement> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<Announcement> findAllActive() {
        return jpaRepository.findAllByStatus("ACTIVE").stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Announcement> findByType(String announcementType) {
        return jpaRepository.findByStatusAndTargetAudience("ACTIVE", announcementType).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Announcement save(Announcement announcement) {
        AnnouncementEntity entity = mapper.toEntity(announcement);
        AnnouncementEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
