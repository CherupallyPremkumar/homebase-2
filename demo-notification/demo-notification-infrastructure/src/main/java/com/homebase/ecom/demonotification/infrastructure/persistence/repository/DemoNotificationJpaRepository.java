package com.homebase.ecom.demonotification.infrastructure.persistence.repository;

import com.homebase.ecom.demonotification.infrastructure.persistence.entity.DemoNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DemoNotificationJpaRepository extends JpaRepository<DemoNotificationEntity, String> {
    List<DemoNotificationEntity> findByOrderId(String orderId);
}
