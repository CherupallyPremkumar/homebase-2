package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CmsScheduleJpaRepository extends JpaRepository<CmsScheduleEntity, String> {
    List<CmsScheduleEntity> findByPageIdAndIsActiveTrue(String pageId);
    List<CmsScheduleEntity> findByPublishAtBeforeAndExecutedFalse(LocalDateTime now);
}
