package com.homebase.ecom.notification.infrastructure.persistence.adapter;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.homebase.ecom.notification.infrastructure.persistence.entity.NotificationEntity;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, String> {

    List<NotificationEntity> findByCustomerId(String customerId);

    List<NotificationEntity> findByChannel(String channel);

    List<NotificationEntity> findByCurrentState(String currentState);
}
