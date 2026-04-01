package com.homebase.ecom.disputes.infrastructure.persistence.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.disputes.infrastructure.persistence.entity.DisputeEntity;

public interface DisputeJpaRepository extends JpaRepository<DisputeEntity, String> {
}
