package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.CmsSeoMetaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CmsSeoMetaJpaRepository extends JpaRepository<CmsSeoMetaEntity, String> {
    Optional<CmsSeoMetaEntity> findByPageId(String pageId);
}
