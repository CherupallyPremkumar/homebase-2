package com.homebase.ecom.demoorder.infrastructure.persistence.repository;

import com.homebase.ecom.demoorder.infrastructure.persistence.entity.DemoOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoOrderJpaRepository extends JpaRepository<DemoOrderEntity, String> {
}
