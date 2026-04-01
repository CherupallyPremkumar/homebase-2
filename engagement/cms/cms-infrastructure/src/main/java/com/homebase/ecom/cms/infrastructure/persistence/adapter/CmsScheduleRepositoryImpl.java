package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.model.CmsSchedule;
import com.homebase.ecom.cms.model.port.CmsScheduleRepository;
import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsScheduleEntity;
import com.homebase.ecom.cms.infrastructure.persistence.mapper.CmsMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CmsScheduleRepositoryImpl implements CmsScheduleRepository {

    private final CmsScheduleJpaRepository jpaRepository;
    private final CmsMapper mapper;

    public CmsScheduleRepositoryImpl(CmsScheduleJpaRepository jpaRepository, CmsMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<CmsSchedule> findById(String id) {
        return jpaRepository.findById(id).map(mapper::toModel);
    }

    @Override
    public List<CmsSchedule> findActiveByPageId(String pageId) {
        return jpaRepository.findByPageIdAndIsActiveTrue(pageId).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<CmsSchedule> findPendingExecution(LocalDateTime now) {
        return jpaRepository.findByPublishAtBeforeAndExecutedFalse(now).stream()
                .map(mapper::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public CmsSchedule save(CmsSchedule schedule) {
        CmsScheduleEntity entity = mapper.toEntity(schedule);
        CmsScheduleEntity savedEntity = jpaRepository.save(entity);
        return mapper.toModel(savedEntity);
    }

    @Override
    public void delete(String id) {
        jpaRepository.deleteById(id);
    }
}
