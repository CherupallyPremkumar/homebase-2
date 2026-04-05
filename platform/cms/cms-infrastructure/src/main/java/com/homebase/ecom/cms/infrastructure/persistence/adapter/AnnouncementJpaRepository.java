package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementJpaRepository extends JpaRepository<AnnouncementEntity, String> {
    List<AnnouncementEntity> findByStatusAndTargetAudience(String status, String targetAudience);
    List<AnnouncementEntity> findAllByStatus(String status);
}
