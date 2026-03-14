package com.homebase.ecom.cms.infrastructure.persistence.adapter;

import com.homebase.ecom.cms.infrastructure.persistence.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerJpaRepository extends JpaRepository<BannerEntity, String> {
    List<BannerEntity> findByPositionAndActiveTrue(String position);
    List<BannerEntity> findAllByActiveTrue();
}
